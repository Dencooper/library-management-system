package com.library.bookservice.mapper;

import com.library.bookservice.dto.request.BookRequest;
import com.library.bookservice.dto.response.BookResponse;
import com.library.bookservice.model.Author;
import com.library.bookservice.model.Book;
import com.library.bookservice.model.Category;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book toEntity(BookRequest request, Author author, Set<Category> categories) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setAuthor(author);
        book.setPrice(request.getPrice());
        book.setCategories(categories);
        return book;
    }

    public BookResponse toResponse(Book book) {
        Set<String> categoryNames = book.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getImageUrl(),
                book.getAuthor().getName(),
                book.getPrice(),
                categoryNames
        );
    }
}
