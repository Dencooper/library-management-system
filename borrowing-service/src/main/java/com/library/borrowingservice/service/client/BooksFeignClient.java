package com.library.borrowingservice.service.client;

import com.library.borrowingservice.constant.BookItemReturnCondition;
import com.library.borrowingservice.dto.response.BookResponse;
import com.library.borrowingservice.dto.response.borrowing.BookItemResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("BOOK-SERVICE")
public interface BooksFeignClient {
    @GetMapping(value = "/api/v1/books/{id}", consumes = "application/json")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id);

    @GetMapping(value = "/api/v1/book-items/{id}", consumes = "application/json")
    public ResponseEntity<BookItemResponse> getBookItemById(@PathVariable Long id);

    @PutMapping(value = "/api/v1/book-items/available/{id}", consumes = "application/json")
    public ResponseEntity<BookItemResponse> updateAvailableBookItem(
            @PathVariable Long id,
            @RequestBody boolean isAvailable
    );

    @PutMapping(value = "/api/v1/book-items/condition/{id}", consumes = "application/json")
    public ResponseEntity<BookItemResponse> updateConditionBookItem(
            @PathVariable Long id,
            @RequestBody BookItemReturnCondition condition
            );
}
