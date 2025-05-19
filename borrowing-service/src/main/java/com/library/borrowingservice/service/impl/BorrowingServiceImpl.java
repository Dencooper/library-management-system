package com.library.borrowingservice.service.impl;

import com.library.borrowingservice.constant.BookItemReturnCondition;
import com.library.borrowingservice.constant.BorrowingRequestStatus;
import com.library.borrowingservice.dto.request.borrowing.BorrowingCreationRequest;
import com.library.borrowingservice.dto.request.penalty.PenaltyCreationRequest;
import com.library.borrowingservice.dto.response.BookResponse;
import com.library.borrowingservice.dto.response.UserResponse;
import com.library.borrowingservice.dto.response.borrowing.BookItemResponse;
import com.library.borrowingservice.dto.response.borrowing.BorrowingResponse;
import com.library.borrowingservice.mapper.BorrowingMapper;
import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.model.BorrowingItem;
import com.library.borrowingservice.model.BorrowingRequest;
import com.library.borrowingservice.repository.BorrowingRepository;
import com.library.borrowingservice.repository.BorrowingRequestRepository;
import com.library.borrowingservice.service.IBorrowingService;
import com.library.borrowingservice.service.IPenaltyService;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.borrowingservice.service.client.BooksFeignClient;
import com.library.borrowingservice.service.client.UsersFeignClient;
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
    private final BorrowingRequestRepository borrowingRequestRepository;
    private final BorrowingMapper borrowingMapper;
    private final BooksFeignClient booksFeignClient;
    private final UsersFeignClient usersFeignClient;
    private final AuthFeignClient authFeignClient;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BorrowingResponse createBorrowing(BorrowingCreationRequest request) {
        BorrowingRequest borrowingRequest = borrowingRequestRepository.findById(request.getBorrowingRequestId())
                .orElseThrow(() -> new RuntimeException("Borrowing request not found"));

        if(borrowingRepository.existsByBorrowingRequestId(borrowingRequest.getId())){
            throw new RuntimeException("This borrowing request already handled.");
        }

        if(checkBannedUser(borrowingRequest.getUserId())){
            throw new RuntimeException("User " + borrowingRequest.getUserId() + "  is banned in system");
        }

        if(borrowingRequest.getAcceptedAt() == null){
            throw new RuntimeException("This borrowing request is not be accepted.");
        }

        if(borrowingRequestRepository.existsByUserIdAndStatus(borrowingRequest.getUserId() ,BorrowingRequestStatus.PENDING)){
            throw new RuntimeException("You have a pending borrowing request");
        }

        Borrowing borrowing = Borrowing.builder()
                .userId(borrowingRequest.getUserId())
                .librarianId(authFeignClient.fetchUser().getBody().getId())
                .borrowedAt(LocalDateTime.now())
                .isLate(false)
                .borrowingRequest(borrowingRequest)
                .build();

        List<BorrowingItem> items = request.getBookItemIds()
                .stream()
                .map(item -> {
                    BookItemResponse bookItem = booksFeignClient.getBookItemById(item).getBody();

                    if(!bookItem.isAvailable()){
                        throw new RuntimeException(bookItem.getCode() + " is not available");
                    }

                    bookItem = booksFeignClient.updateAvailableBookItem(item, false).getBody();
                    return BorrowingItem.builder()
                            .bookItemId(bookItem.getId())
                            .bookItemCondition(bookItem.getBookItemCondition())
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
    @Transactional(readOnly = true)
    public BorrowingResponse getBorrowingById(Long id) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing not found"));
        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BorrowingResponse confirmReturn(Long borrowingId, Borrowing request) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Borrowing not found"));

        double amount = 0.0;

        for(BorrowingItem item : request.getItems()){
            if(item.getBookItemCondition().equals(BookItemReturnCondition.LOST) ||
                    item.getBookItemCondition().equals(BookItemReturnCondition.MAJOR_DAMAGE)){
                BookItemResponse bookItemResponse = booksFeignClient.getBookItemById(item.getBookItemId()).getBody();
                booksFeignClient.updateConditionBookItem(item.getBookItemId(), item.getBookItemCondition());
                amount += bookItemResponse.getPrice();
            } else {
                booksFeignClient.updateAvailableBookItem(item.getBookItemId(), true);
            }
        }

        if(amount > 0.0){
            penaltyService.createPenalty(PenaltyCreationRequest.builder()
                            .borrowingId(borrowingId)
                            .amount(amount)
                            .description(request.getPenaltyDescription())
                            .build()
            );
        }

        borrowing.setReturnedAt(LocalDateTime.now());
        if(LocalDateTime.now().isAfter(borrowing.getBorrowedAt().plusDays(30))){
            borrowing.setIsLate(true);
        }
        borrowingRepository.save(borrowing);
        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowingResponse> getBorrowingsByUser(String email) {
        UserResponse user = usersFeignClient.getUserByEmail(email).getBody();

        return borrowingRepository.findAllByUserId(user.getId()).stream()
                .map(borrowingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkBannedUser(Long userId) {
        List<Borrowing> borrowings = borrowingRepository.findAllByUserId(userId);
        int count = 0;
        for (Borrowing borrowing : borrowings){
            if(borrowing.getIsLate() || borrowing.getPenalty() != null){
                count++;
            }
        }
        return count >= 3;
    }

}
