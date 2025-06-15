package com.library.borrowingservice.service.client;

import com.library.borrowingservice.dto.response.borrowing.BookItemResponse;
import com.library.commonservice.dto.ApiResponse;
import com.library.commonservice.dto.response.BookResponse;
import com.library.commonservice.utils.constant.BookItemCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("BOOK-SERVICE")
public interface BooksFeignClient {
    @GetMapping(value = "/api/v1/books/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(@PathVariable Long id);

    @GetMapping(value = "/api/v1/book-items/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<BookItemResponse>> getBookItemById(@PathVariable Long id);

    @PutMapping(value = "/api/v1/book-items/available/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<BookItemResponse>> updateAvailableBookItem(
            @PathVariable Long id,
            @RequestBody boolean isAvailable
    );

    @PutMapping(value = "/api/v1/book-items/condition/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<BookItemResponse>> updateConditionBookItem(
            @PathVariable Long id,
            @RequestBody BookItemCondition condition
            );
}
