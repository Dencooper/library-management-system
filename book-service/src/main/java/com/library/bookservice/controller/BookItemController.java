package com.library.bookservice.controller;

import com.library.bookservice.dto.request.BookItemCreationRequest;
import com.library.bookservice.dto.response.BookItemResponse;
import com.library.bookservice.service.IBookItemService;
import com.library.commonservice.dto.request.BookItemUpdateRequest;
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
    public ResponseEntity<List<BookItemResponse>> getAllBookItems(
            @RequestParam(required = false) Boolean isAvailable
    ) {
        return ResponseEntity.ok(bookItemService.getAllBookItems(isAvailable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookItemResponse> getBookItemById(@PathVariable Long id) {
        return ResponseEntity.ok(bookItemService.getBookItemById(id));
    }

    @GetMapping("/code")
    public ResponseEntity<BookItemResponse> getBookItemByCode(@RequestParam String code) {
        return ResponseEntity.ok(bookItemService.getBookItemByCode(code));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookItemResponse>> getBookItemsByBookId(
            @PathVariable Long bookId,
            @RequestParam(required = false) Boolean isAvailable) {
        return ResponseEntity.ok(bookItemService.getBookItemsByBook(bookId, isAvailable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookItemResponse> updateBookItem(
            @PathVariable Long id,
            @Valid @RequestBody BookItemUpdateRequest request
    ) {
        return ResponseEntity.ok(bookItemService.updateBookItem(id, request));
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
