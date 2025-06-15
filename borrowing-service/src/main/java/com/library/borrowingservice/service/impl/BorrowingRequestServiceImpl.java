package com.library.borrowingservice.service.impl;

import com.library.borrowingservice.dto.request.borrowingRequest.BorrowingRequestCreationRequest;
import com.library.borrowingservice.mapper.BorrowingRequestMapper;
import com.library.borrowingservice.model.BorrowingRequest;
import com.library.borrowingservice.model.BorrowingRequestItem;
import com.library.borrowingservice.repository.BorrowingRequestRepository;
import com.library.borrowingservice.service.IBorrowingRequestService;
import com.library.borrowingservice.service.IBorrowingService;
import com.library.borrowingservice.service.client.AuthFeignClient;
import com.library.commonservice.dto.response.BorrowingRequestResponse;
import com.library.commonservice.service.KafkaService;
import com.library.commonservice.utils.constant.BorrowingRequestStatus;
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
    private final KafkaService kafkaService;

    @Override
    @Transactional
    public BorrowingRequestResponse createBorrowingRequest(BorrowingRequestCreationRequest request) {
        BorrowingRequest borrowingRequest = BorrowingRequest.builder()
                .userId(authFeignClient.fetchUser().getBody().getData().getId())
                .build();

        if (borrowingRequestRepository.existsByUserIdAndStatus(borrowingRequest.getUserId(),
                BorrowingRequestStatus.PENDING)) {
            throw new RuntimeException("You have a pending borrowing request");
        }

        if (iBorrowingService.checkBannedUser(borrowingRequest.getUserId())) {
            throw new RuntimeException("User " + borrowingRequest.getUserId() + "  is banned in system");
        }

        List<BorrowingRequestItem> borrowingRequestItems = request.getBookIds()
                .stream()
                .map(bookId -> BorrowingRequestItem.builder()
                        .bookId(bookId)
                        .borrowingRequest(borrowingRequest)
                        .build())
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

        if (!borrowingRequest.getStatus().equals(request.getStatus())
                && !borrowingRequest.getStatus().equals(BorrowingRequestStatus.APPROVED)
                && !borrowingRequest.getStatus().equals(BorrowingRequestStatus.REJECTED)) {

            borrowingRequest.setStatus(request.getStatus());
            borrowingRequest.setLibrarianId(authFeignClient.fetchUser().getBody().getData().getId());
            if (request.getStatus().equals(BorrowingRequestStatus.APPROVED)) {
                borrowingRequest.setAcceptedAt(LocalDateTime.now());
            }

            if (request.getStatus().equals(BorrowingRequestStatus.REJECTED)) {
                borrowingRequest.setRejectedAt(LocalDateTime.now());
            }
        }
        borrowingRequestRepository.save(borrowingRequest);
        BorrowingRequestResponse response = borrowingRequestMapper.toResponse(borrowingRequest);
        if (request.getStatus().equals(BorrowingRequestStatus.APPROVED)) {
            kafkaService.sendMessage("notification", response);
        }
        return response;
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

    @Override
    public List<BorrowingRequestResponse> getMyAllBorrowingRequests() {
        return borrowingRequestRepository.findAllByUserId(authFeignClient.fetchUser().getBody().getData().getId())
                .stream()
                .map(borrowingRequestMapper::toResponse)
                .toList();
    }
}
