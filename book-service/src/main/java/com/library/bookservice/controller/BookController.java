package com.library.bookservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.bookservice.dto.request.BookRequest;
import com.library.bookservice.dto.response.BookResponse;
import com.library.bookservice.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService bookService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookResponse> createBook(
            @RequestPart("book") String bookJson,
            @RequestParam("image") MultipartFile imageFile
    )throws JsonProcessingException{
        BookRequest request = objectMapper.readValue(bookJson, BookRequest.class);
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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestPart("book") String bookJson
    ) throws JsonProcessingException {
        BookRequest request = objectMapper.readValue(bookJson, BookRequest.class);
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookResponse> updateBookImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile imageFile
    ){
        return ResponseEntity.ok(bookService.updateBookImage(id, imageFile));
    }

    @GetMapping("/quantity")
    public ResponseEntity<Long> getUserQuantity(){
        return ResponseEntity.ok().body(bookService.getBookQuantity());
    }
}