package com.library.borrowingservice.repository;

import com.library.borrowingservice.constant.BorrowingRequestStatus;
import com.library.borrowingservice.model.BorrowingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRequestRepository extends JpaRepository<BorrowingRequest, Long> {
    boolean existsByUserIdAndStatus(Long userId, BorrowingRequestStatus borrowingRequestStatus);
}
