package com.library.borrowingservice.controller;

import com.library.borrowingservice.dto.request.borrowing.BorrowingCreationRequest;
import com.library.borrowingservice.dto.request.borrowing.ReturnBookRequest;
import com.library.borrowingservice.dto.response.borrowing.BorrowingResponse;
import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.service.IBorrowingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowings")
@RequiredArgsConstructor
public class BorrowingController {
    private final IBorrowingService iBorrowingService;

    @PostMapping
    public ResponseEntity<BorrowingResponse> createBorrowing(@Valid @RequestBody BorrowingCreationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(iBorrowingService.createBorrowing(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingResponse> getBorrowingById(@PathVariable Long id){
        return ResponseEntity.ok().body(iBorrowingService.getBorrowingById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<List<BorrowingResponse>> getBorrowingsByUser(@RequestParam String email){
        return ResponseEntity.ok().body(iBorrowingService.getBorrowingsByUser(email));
    }

    @GetMapping
    public ResponseEntity<List<BorrowingResponse>> getAllBorrowings(){
        return ResponseEntity.ok().body(iBorrowingService.getAllBorrowings());
    }

    @GetMapping("/user")
    public ResponseEntity<List<BorrowingResponse>> getMyAllBorrowings(){
        return ResponseEntity.ok().body(iBorrowingService.getMyAllBorrowings());
    }

    @PatchMapping("/return")
    ResponseEntity<BorrowingResponse> returnBook(@Valid @RequestBody ReturnBookRequest request){
        return ResponseEntity.ok().body(iBorrowingService.returnBook(request));
    }

    @GetMapping("/quantity")
    public ResponseEntity<Long> getBorrowingQuantity(){
        return ResponseEntity.ok().body(iBorrowingService.getBorrowingQuantity());
    }
}
