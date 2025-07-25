package com.library.bookservice.mapper;

import com.library.bookservice.dto.request.BookItemCreationRequest;
import com.library.bookservice.dto.response.BookItemResponse;
import com.library.bookservice.model.Book;
import com.library.bookservice.model.BookItem;
import org.springframework.stereotype.Component;

@Component
public class BookItemMapper {
    public BookItem toEntity(BookItemCreationRequest request, Book book) {
        return BookItem.builder()
                .code(request.getCode())
                .isAvailable(true)
                .book(book)
                .bookItemCondition(request.getBookItemCondition())
                .build();
    }

    public BookItemResponse toResponse(BookItem item) {
        return BookItemResponse.builder()
                .id(item.getId())
                .code(item.getCode())
                .isAvailable(item.isAvailable())
                .bookTitle(item.getBook().getTitle())
                .bookId(item.getBook().getId())
                .price(item.getBook().getPrice())
                .bookItemCondition(item.getBookItemCondition())
                .build();
    }
}
