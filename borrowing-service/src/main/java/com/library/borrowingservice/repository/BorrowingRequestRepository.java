package com.library.borrowingservice.repository;

import com.library.borrowingservice.model.BorrowingRequest;
import com.library.commonservice.utils.constant.BorrowingRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRequestRepository extends JpaRepository<BorrowingRequest, Long> {
    boolean existsByUserIdAndStatus(Long userId, BorrowingRequestStatus borrowingRequestStatus);
    List<BorrowingRequest> findAllByUserId(Long userId);
}
