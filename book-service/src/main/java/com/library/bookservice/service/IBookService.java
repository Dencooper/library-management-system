package com.library.bookservice.service;

import com.library.bookservice.dto.request.BookRequest;
import com.library.bookservice.dto.response.BookResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBookService {
    BookResponse createBook(BookRequest request, MultipartFile imageFile);
    BookResponse getBookById(Long id);
    List<BookResponse> getAllBooks();
    BookResponse updateBook(Long bookId, BookRequest request);
    BookResponse updateBookImage(Long bookId, MultipartFile imageFile);
    Long getBookQuantity();
}
