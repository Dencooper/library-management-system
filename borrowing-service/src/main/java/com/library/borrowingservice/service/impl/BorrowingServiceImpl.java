package com.library.borrowingservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.borrowingservice.dto.request.borrowing.BorrowingCreationRequest;
import com.library.borrowingservice.dto.request.borrowing.ReturnBookRequest;
import com.library.borrowingservice.dto.request.penalty.PenaltyCreationRequest;
import com.library.borrowingservice.dto.response.borrowing.*;
import com.library.borrowingservice.mapper.BorrowingMapper;
import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.model.BorrowingItem;
import com.library.borrowingservice.repository.BorrowingItemRepository;
import com.library.borrowingservice.repository.BorrowingRepository;
import com.library.borrowingservice.service.IBorrowingService;
import com.library.borrowingservice.service.IPenaltyService;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.borrowingservice.service.client.BooksFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
import com.library.commonservice.dto.request.BookItemUpdateRequest;
import com.library.commonservice.dto.response.BookResponse;
import com.library.commonservice.dto.response.UserResponse;
import com.library.commonservice.service.KafkaService;
import com.library.commonservice.utils.constant.BookItemCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements IBorrowingService {
    private final IPenaltyService penaltyService;
    private final BorrowingRepository borrowingRepository;
    private final BorrowingItemRepository borrowingItemRepository;
    private final BorrowingMapper borrowingMapper;
    private final BooksFeignClient booksFeignClient;
    private final UsersFeignClient usersFeignClient;
    private final AuthFeignClient authFeignClient;
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BorrowingResponse createBorrowing(BorrowingCreationRequest request) {
        if(borrowingRepository.existsByUserIdAndReturnedAt(usersFeignClient.getUserByEmail(request.getEmail()).getBody().getData().getId(), null)){
            throw new RuntimeException("This user has a active or overdue borrowing");
        };

        Borrowing borrowing = Borrowing.builder()
                .userId(usersFeignClient.getUserByEmail(request.getEmail()).getBody().getData().getId())
                .librarianId(authFeignClient.fetchUser().getBody().getData().getId())
                .borrowedAt(LocalDateTime.now())
                .isLate(false)
                .description(request.getDescription())
                .build();

        List<BorrowingItem> items = request.getBookItemIds()
                .stream()
                .map(item -> {
                    BookItemResponse bookItem = booksFeignClient.getBookItemById(item).getBody().getData();
                    if (!bookItem.isAvailable()) {
                        throw new RuntimeException(bookItem.getCode() + " is not available");
                    }
                    BookItemUpdateRequest bookItemUpdateRequest = BookItemUpdateRequest.builder()
                            .bookItemCondition(bookItem.getBookItemCondition())
                            .isAvailable(false)
                            .build();
                    bookItem = booksFeignClient.updateBookItem(bookItem.getId(), bookItemUpdateRequest).getBody().getData();
                    return BorrowingItem.builder()
                            .bookItemId(bookItem.getId())
                            .borrowing(borrowing)
                            .build();
                })
                .toList();

        borrowing.setItems(items);
        borrowingRepository.save(borrowing);
        kafkaService.sendMessage("borrowingNotification", borrowingMapper.toResponse(borrowing));
        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> getAllBorrowings() {
        return borrowingRepository.findAll().stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BorrowingResponse> getMyAllBorrowings() {
        return  borrowingRepository.findAllByUserId(authFeignClient.fetchUser().getBody().getData().getId())
                .stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowingResponse getBorrowingById(Long id) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing not found"));
        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BorrowingResponse returnBook(ReturnBookRequest request) {
        Borrowing borrowing = borrowingRepository.findById(request.getBorrowingId())
                .orElseThrow(() -> new RuntimeException("Borrowing not found"));

        double amount = 0.0;

        for (ReturnBookRequest.ReturnBookItemDTO item : request.getItems()) {
            BookItemUpdateRequest bookItemUpdateRequest = BookItemUpdateRequest.builder()
                    .bookItemCondition(item.getBookItemCondition())
                    .build();
            if (item.getBookItemCondition().equals(BookItemCondition.LOST) ||
                    item.getBookItemCondition().equals(BookItemCondition.MAJOR_DAMAGE)) {
                bookItemUpdateRequest.setIsAvailable(false);
                BookItemResponse bookItemResponse = booksFeignClient.getBookItemById(item.getBookItemId()).getBody()
                        .getData();
                booksFeignClient.updateBookItem(item.getBookItemId(), bookItemUpdateRequest);
                amount += bookItemResponse.getPrice();
            } else {
                bookItemUpdateRequest.setIsAvailable(true);
                booksFeignClient.updateBookItem(item.getBookItemId(), bookItemUpdateRequest);
            }
        }

        if (amount > 0.0) {
            penaltyService.createPenalty(PenaltyCreationRequest.builder()
                    .borrowingId(request.getBorrowingId())
                    .amount(amount)
                    .description(request.getDescription())
                    .build());
        }

        borrowing.setReturnedAt(LocalDateTime.now());
        if (LocalDateTime.now().isAfter(borrowing.getBorrowedAt().plusDays(30))) {
            borrowing.setIsLate(true);
        }
        borrowingRepository.save(borrowing);
        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public List<BorrowingResponse> getBorrowingsByUser(String email) {
        UserResponse user = usersFeignClient.getUserByEmail(email).getBody().getData();

        return borrowingRepository.findAllByUserId(user.getId()).stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public Boolean checkBannedUser(Long userId) {
        List<Borrowing> borrowings = borrowingRepository.findAllByUserId(userId);
        int count = 0;
        for (Borrowing borrowing : borrowings) {
            if (borrowing.getIsLate() || borrowing.getPenalty() != null) {
                count++;
            }
        }
        return count >= 3;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getBorrowingQuantity() {
        return borrowingRepository.count();
    }

    @Override
    public Void sendReturnReminderBorrowingEmail() {
        List<Borrowing> borrowingList = borrowingRepository.findAllByReturnedAtIsNull();
        if(borrowingList != null && !borrowingList.isEmpty()) {
            for(Borrowing borrowing : borrowingList) {
                LocalDateTime borrowedPlus27 = borrowing.getBorrowedAt().plusDays(27);
                if(borrowedPlus27.isBefore(LocalDateTime.now()) || borrowedPlus27.isEqual(LocalDateTime.now())){
                    kafkaService.sendMessage("returnReminderNotification", borrowingMapper.toResponse(borrowing));
                }
            }
        }
        return null;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BorrowingStatisticsResponse doBorrowingStatistics(LocalDateTime from, LocalDateTime to) {
        List<Borrowing> borrowings = borrowingRepository.findAllByBorrowedAtBetween(from, to);
        long onTimeReturns = 0;
        long lateReturns = 0;
        long activeBorrowings = 0;
        long overdueBorrowings = 0;
        long totalBorrowDuration = 0;
        for (Borrowing borrowing : borrowings) {
            if(borrowing.getReturnedAt() != null) {
                if(borrowing.getIsLate()) {
                    lateReturns += 1;
                } else {
                    onTimeReturns += 1;
                }
                totalBorrowDuration += Duration.between(borrowing.getBorrowedAt(), borrowing.getReturnedAt()).toDays() + 1;
            }  else {
                if(borrowing.getBorrowedAt().plusDays(30).isBefore(LocalDateTime.now())) {
                    overdueBorrowings += 1;
                } else {
                    activeBorrowings += 1;
                }
            }
        }
        return BorrowingStatisticsResponse.builder()
                .totalBorrowings(borrowings.size())
                .activeBorrowing(activeBorrowings)
                .overdueBorrowing(overdueBorrowings)
                .onTimeReturns(onTimeReturns)
                .lateReturns(lateReturns)
                .averageBorrowDuration((double) totalBorrowDuration /(lateReturns + onTimeReturns))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BookStatisticsResponse doBookStatistic(LocalDateTime from, LocalDateTime to) {
        Map<Long, Long> bookItemIdToCount = new HashMap<>();
        List<Borrowing> borrowings = borrowingRepository.findAllByBorrowedAtBetween(from, to);
        List<BookItemResponse> bookItemResponses = booksFeignClient.getAllBookItems().getBody().getData();
        long availableBooks = bookItemResponses.stream()
                .filter(BookItemResponse::isAvailable)
                .count();
        long damagedBook = bookItemResponses.stream()
                .filter(bookItem -> bookItem.getBookItemCondition() == BookItemCondition.MAJOR_DAMAGE)
                .count();
        long lostBook = bookItemResponses.stream()
                .filter(bookItem -> bookItem.getBookItemCondition() == BookItemCondition.LOST)
                .count();
        long borrowedBooks = bookItemResponses.stream()
                .filter(bookItem -> borrowingItemRepository.existsByBookItemId(bookItem.getId()))
                .count();
        for (Borrowing b : borrowings) {
            for (BorrowingItem item : b.getItems()) {
                Long bookItemId = item.getBookItemId();
                bookItemIdToCount.put(bookItemId, bookItemIdToCount.getOrDefault(bookItemId, 0L) + 1);
            }
        }
        Map<Long, Long> bookItemIdToBookId = booksFeignClient.getBookIdsByItemIds(new ArrayList<>(bookItemIdToCount.keySet())).getBody().getData();
        Map<Long, Long> bookIdToBorrowCount = new HashMap<>();
        for (Map.Entry<Long, Long> entry : bookItemIdToCount.entrySet()) {
            Long bookItemId = entry.getKey();
            Long borrowCount = entry.getValue();
            Long bookId = bookItemIdToBookId.get(bookItemId);
            if (bookId != null) {
                bookIdToBorrowCount.put(bookId, bookIdToBorrowCount.getOrDefault(bookId, 0L) + borrowCount);
            }
        }
        return BookStatisticsResponse.builder()
                .totalBooks(bookItemResponses.size())
                .borrowedBooks(borrowedBooks)
                .availableBooks(availableBooks)
                .damagedBooks(damagedBook)
                .lostBooks(lostBook)
                .popularBooks(
                        bookIdToBorrowCount.entrySet().stream()
                                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                                .limit(10)
                                .map(entry -> {
                                    Long bookId = entry.getKey();
                                    Long borrowCount = entry.getValue();
                                    BookResponse book = booksFeignClient.getBook(bookId).getBody().getData();

                                    return new BookStatisticsResponse.bookDTO(
                                            book.getTitle(),
                                            book.getAuthor().getName(),
                                            borrowCount
                                    );
                                })
                                .collect(Collectors.toList())
                )
                .availabilityRate(((double) availableBooks /bookItemResponses.size()) * 100)
                .utilizationRate(((double) borrowedBooks /bookItemResponses.size()) * 100)
                .build();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public UserStatisticsResponse doUserStatistic(LocalDateTime from, LocalDateTime to) {
        List<UserResponse> userResponses = usersFeignClient.getAllUsers().getBody().getData();
        List<Borrowing> borrowings = borrowingRepository.findAllByBorrowedAtBetween(from, to);
        long activeUsers = userResponses.size() - userResponses.stream()
                .filter(UserResponse::getIsBanned)
                .count();
        long newRegistrations = userResponses.stream()
                .filter(userResponse ->
                        (userResponse.getCreatedAt().isAfter(from) || userResponse.getCreatedAt().equals(from)) &&
                                (userResponse.getCreatedAt().isBefore(to) || userResponse.getCreatedAt().equals(to)))
                .count();
        Map<Long, Long> userIdToBorrowCount = new HashMap<>();
        for (Borrowing b : borrowings) {
            Long userId = b.getUserId();
            userIdToBorrowCount.put(userId, userIdToBorrowCount.getOrDefault(userId, 0L) + 1);
        }
        return UserStatisticsResponse.builder()
                .totalUsers(userResponses.size())
                .activeUsers(activeUsers)
                .newRegistrations(newRegistrations)
                .avgBorrowsPerUser((double) borrowings.size() / userResponses.size())
                .topActiveUsers(userIdToBorrowCount.entrySet().stream()
                        .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                        .limit(3)
                        .map(entry -> {
                            Long userId = entry.getKey();
                            Long borrowCount = entry.getValue();
                            UserResponse user = usersFeignClient.getUserById(userId).getBody().getData();
                            return new UserStatisticsResponse.userDTO(
                                    userId,
                                    user.getEmail(),
                                    user.getFullName(),
                                    borrowCount
                            );
                        })
                        .collect(Collectors.toList())
                )
                .build();
    }
}
