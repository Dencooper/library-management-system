package com.library.borrowingservice.service.client;

import com.library.borrowingservice.dto.response.borrowing.BookItemResponse;
import com.library.commonservice.dto.ApiResponse;
import com.library.commonservice.dto.request.BookItemUpdateRequest;
import com.library.commonservice.dto.response.BookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient("BOOK-SERVICE")
public interface BooksFeignClient {
    @GetMapping(value = "/api/v1/books/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(@PathVariable Long id);

    @GetMapping(value = "/api/v1/book-items/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<BookItemResponse>> getBookItemById(@PathVariable Long id);

    @PutMapping(value = "/api/v1/book-items/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<BookItemResponse>> updateBookItem(
            @PathVariable Long id,
            @RequestBody BookItemUpdateRequest request
    );

    @GetMapping(value = "/api/v1/book-items", consumes = "application/json")
    public ResponseEntity<ApiResponse<List<BookItemResponse>>> getAllBookItems();

    @PostMapping(value = "/api/v1/book-items/book-ids")
    public ResponseEntity<ApiResponse<Map<Long, Long>>> getBookIdsByItemIds(@RequestBody List<Long> bookItemIds);
}
