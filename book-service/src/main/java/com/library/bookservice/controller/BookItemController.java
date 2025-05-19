package com.library.bookservice.controller;

import com.library.bookservice.constant.BookItemCondition;
import com.library.bookservice.dto.request.BookItemCreationRequest;
import com.library.bookservice.dto.request.BookItemUpdateRequest;
import com.library.bookservice.dto.response.BookItemResponse;
import com.library.bookservice.service.IBookItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-items")
@RequiredArgsConstructor
public class BookItemController {
    private final IBookItemService bookItemService;

    @PostMapping
    public ResponseEntity<BookItemResponse> createBookItem(@Valid @RequestBody BookItemCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookItemService.createBookItem(request));
    }

    @GetMapping
    public ResponseEntity<List<BookItemResponse>> getAllBookItems() {
        return ResponseEntity.ok(bookItemService.getAllBookItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookItemResponse> getBookItemById(@PathVariable Long id) {
        return ResponseEntity.ok(bookItemService.getBookItemById(id));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookItemResponse>> getBookItemsByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookItemService.getBookItemsByBookId(bookId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookItemResponse> updateBookItem(
            @PathVariable Long id,
            @Valid @RequestBody BookItemUpdateRequest request
    ) {
        return ResponseEntity.ok(bookItemService.updateBookItem(id, request));
    }

    @PutMapping("/available/{id}")
    public ResponseEntity<BookItemResponse> updateAvailableBookItem(
            @PathVariable Long id,
            @RequestBody boolean isAvailable
    ) {
        return ResponseEntity.ok(bookItemService.updateAvailableBookItem(id, isAvailable));
    }

    @PutMapping("/condition/{id}")
    public ResponseEntity<BookItemResponse> updateConditionBookItem(
            @PathVariable Long id,
            @RequestBody BookItemCondition condition
            ){
        return ResponseEntity.ok(bookItemService.updateConditionBookItem(id, condition));
    }



    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> deleteBookItem(
            @PathVariable Long id,
            @RequestParam(defaultValue = "true") boolean isDeleted
    ) {
        bookItemService.deleteBookItem(id, isDeleted);
        return ResponseEntity.noContent().build();
    }
}
