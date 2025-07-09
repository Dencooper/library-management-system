package com.library.borrowingservice.service.impl;

import com.library.borrowingservice.dto.request.borrowing.BorrowingCreationRequest;
import com.library.borrowingservice.dto.request.borrowing.ReturnBookRequest;
import com.library.borrowingservice.dto.request.penalty.PenaltyCreationRequest;
import com.library.borrowingservice.dto.response.borrowing.BookItemResponse;
import com.library.borrowingservice.dto.response.borrowing.BorrowingResponse;
import com.library.borrowingservice.mapper.BorrowingMapper;
import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.model.BorrowingItem;
import com.library.borrowingservice.repository.BorrowingRepository;
import com.library.borrowingservice.service.IBorrowingService;
import com.library.borrowingservice.service.IPenaltyService;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.borrowingservice.service.client.BooksFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
import com.library.commonservice.dto.request.BookItemUpdateRequest;
import com.library.commonservice.dto.response.UserResponse;
import com.library.commonservice.utils.constant.BookItemCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements IBorrowingService {
    private final IPenaltyService penaltyService;
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;
    private final BooksFeignClient booksFeignClient;
    private final UsersFeignClient usersFeignClient;
    private final AuthFeignClient authFeignClient;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BorrowingResponse createBorrowing(BorrowingCreationRequest request) {
        if(borrowingRepository.existsByUserIdAndReturnedAt(usersFeignClient.getUserByEmail(request.getEmail()).getBody().getData().getId(), null)){
            throw new RuntimeException("This user has a active borrowing");
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
    public List<BorrowingResponse> getBorrowingsByUser(String email) {
        UserResponse user = usersFeignClient.getUserByEmail(email).getBody().getData();

        return borrowingRepository.findAllByUserId(user.getId()).stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
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
}
