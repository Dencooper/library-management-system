package com.library.bookservice.controller;

import com.library.bookservice.dto.request.BookRequest;
import com.library.bookservice.dto.response.BookResponse;
import com.library.bookservice.service.IBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookResponse> createBook(
            @RequestPart("book") @Valid BookRequest request,
            @RequestPart("image") MultipartFile imageFile) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(request, imageFile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody @Valid BookRequest request
    ){
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> updateBookImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile imageFile
    ){
        return ResponseEntity.ok(bookService.updateBookImage(id, imageFile));
    }
}