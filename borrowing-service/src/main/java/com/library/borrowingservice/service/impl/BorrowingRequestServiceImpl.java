package com.library.borrowingservice.service.impl;

import com.library.borrowingservice.constant.BorrowingRequestStatus;
import com.library.borrowingservice.dto.request.borrowingRequest.BorrowingRequestCreationRequest;
import com.library.borrowingservice.dto.response.borrowingRequest.BorrowingRequestResponse;
import com.library.borrowingservice.mapper.BorrowingRequestMapper;
import com.library.borrowingservice.model.BorrowingRequest;
import com.library.borrowingservice.model.BorrowingRequestItem;
import com.library.borrowingservice.repository.BorrowingRepository;
import com.library.borrowingservice.repository.BorrowingRequestRepository;
import com.library.borrowingservice.service.IBorrowingRequestService;
import com.library.borrowingservice.service.IBorrowingService;
import com.library.borrowingservice.service.client.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowingRequestServiceImpl implements IBorrowingRequestService {
    private final BorrowingRequestRepository borrowingRequestRepository;
    private final BorrowingRequestMapper borrowingRequestMapper;
    private final IBorrowingService iBorrowingService;
    private final AuthFeignClient authFeignClient;


    @Override
    @Transactional
    public BorrowingRequestResponse createBorrowingRequest(BorrowingRequestCreationRequest request) {
        BorrowingRequest borrowingRequest = BorrowingRequest.builder()
                .userId(authFeignClient.fetchUser().getBody().getId())
                .build();

        if(borrowingRequestRepository.existsByUserIdAndStatus(borrowingRequest.getUserId(), BorrowingRequestStatus.PENDING)
        ){
            throw new RuntimeException("You have a pending borrowing request");
        }

        if(iBorrowingService.checkBannedUser(borrowingRequest.getUserId())){
            throw new RuntimeException("User " + borrowingRequest.getUserId() + "  is banned in system");
        }

        List<BorrowingRequestItem> borrowingRequestItems = request.getBookIds()
                .stream()
                .map(bookId -> BorrowingRequestItem.builder()
                        .bookId(bookId)
                        .borrowingRequest(borrowingRequest)
                        .build()
                )
                .toList();

        borrowingRequest.setRequestedAt(LocalDateTime.now());
        borrowingRequest.setStatus(BorrowingRequestStatus.PENDING);
        borrowingRequest.setItems(borrowingRequestItems);
        borrowingRequestRepository.save(borrowingRequest);
        return borrowingRequestMapper.toResponse(borrowingRequest);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public BorrowingRequestResponse updateBorrowingRequest(Long id, BorrowingRequest request) {
        BorrowingRequest borrowingRequest = borrowingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing Request not found"));

        borrowingRequest.setDescription(request.getDescription());

        if(!borrowingRequest.getStatus().equals(request.getStatus())
                && !borrowingRequest.getStatus().equals(BorrowingRequestStatus.APPROVED)
                && !borrowingRequest.getStatus().equals(BorrowingRequestStatus.REJECTED)){

            borrowingRequest.setStatus(request.getStatus());
            borrowingRequest.setLibrarianId(authFeignClient.fetchUser().getBody().getId());
            if(request.getStatus().equals(BorrowingRequestStatus.APPROVED)){
                borrowingRequest.setAcceptedAt(LocalDateTime.now());
            }

            if(request.getStatus().equals(BorrowingRequestStatus.REJECTED)){
                borrowingRequest.setRejectedAt(LocalDateTime.now());
            }
        }
        borrowingRequestRepository.save(borrowingRequest);
        return borrowingRequestMapper.toResponse(borrowingRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowingRequestResponse getBorrowingRequestById(Long id) {
        BorrowingRequest borrowingRequest = borrowingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowing request not found"));
        return borrowingRequestMapper.toResponse(borrowingRequest);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public List<BorrowingRequestResponse> getAllBorrowingRequests() {
        return borrowingRequestRepository.findAll().stream()
                .map(borrowingRequestMapper::toResponse)
                .toList();
    }
}
