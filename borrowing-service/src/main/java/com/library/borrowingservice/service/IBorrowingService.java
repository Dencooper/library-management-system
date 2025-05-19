package com.library.borrowingservice.service;

import com.library.borrowingservice.dto.request.borrowing.BorrowingCreationRequest;
import com.library.borrowingservice.dto.response.borrowing.BorrowingResponse;
import com.library.borrowingservice.model.Borrowing;

import java.util.List;

public interface IBorrowingService {
    BorrowingResponse createBorrowing(BorrowingCreationRequest request);
    BorrowingResponse confirmReturn(Long borrowingId, Borrowing request);
    List<BorrowingResponse> getBorrowingsByUser(String email);
    List<BorrowingResponse> getAllBorrowings();
    BorrowingResponse getBorrowingById(Long id);
    Boolean checkBannedUser(Long userId);
}
