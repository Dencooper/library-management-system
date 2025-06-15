package com.library.borrowingservice.controller;

import com.library.borrowingservice.dto.request.borrowingRequest.BorrowingRequestCreationRequest;
import com.library.borrowingservice.model.BorrowingRequest;
import com.library.borrowingservice.service.IBorrowingRequestService;
import com.library.borrowingservice.service.IBorrowingService;
import com.library.commonservice.dto.response.BorrowingRequestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowing-requests")
@RequiredArgsConstructor
public class BorrowingRequestController {
    private final IBorrowingRequestService iBorrowingRequestService;

    @PostMapping
    public ResponseEntity<BorrowingRequestResponse> createBorrowingRequest(@RequestBody BorrowingRequestCreationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(iBorrowingRequestService.createBorrowingRequest(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingRequestResponse> getBorrowingRequestById(@PathVariable Long id){
        return ResponseEntity.ok().body(iBorrowingRequestService.getBorrowingRequestById(id));
    }

    @GetMapping
    public ResponseEntity<List<BorrowingRequestResponse>> getAllBorrowingRequests(){
        return ResponseEntity.ok().body(iBorrowingRequestService.getAllBorrowingRequests());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BorrowingRequestResponse> updateBorrowingRequest(
            @Valid @RequestBody BorrowingRequest request,
            @PathVariable Long id){
        return ResponseEntity.ok().body(iBorrowingRequestService.updateBorrowingRequest(id, request));
    }

    @GetMapping("/user")
    public ResponseEntity<List<BorrowingRequestResponse>> getMyAllBorrowingRequests(){
        return ResponseEntity.ok().body(iBorrowingRequestService.getMyAllBorrowingRequests());
    }
}
