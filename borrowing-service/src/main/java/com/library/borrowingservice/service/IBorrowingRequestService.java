package com.library.borrowingservice.service;

import com.library.borrowingservice.dto.request.borrowingRequest.BorrowingRequestCreationRequest;
import com.library.borrowingservice.model.BorrowingRequest;
import com.library.commonservice.dto.response.BorrowingRequestResponse;

import java.util.List;

public interface IBorrowingRequestService {
    BorrowingRequestResponse createBorrowingRequest(BorrowingRequestCreationRequest request);
    BorrowingRequestResponse updateBorrowingRequest(Long id, BorrowingRequest request);
    BorrowingRequestResponse getBorrowingRequestById(Long id);
    List<BorrowingRequestResponse> getAllBorrowingRequests();
    List<BorrowingRequestResponse> getMyAllBorrowingRequests();
}
