package com.library.bookservice.mapper;

import com.library.bookservice.dto.request.BookRequest;
import com.library.bookservice.dto.response.BookResponse;
import com.library.bookservice.model.Author;
import com.library.bookservice.model.Book;
import com.library.bookservice.model.BookItem;
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
        Set<BookResponse.CategoryDTO> categories = book.getCategories()
                .stream()
                .map((category) -> {
                    return new BookResponse.CategoryDTO(
                            category.getId(),
                            category.getName()
                    );
                })
                .collect(Collectors.toSet());

        return  BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .imageUrl(book.getImageUrl())
                .author(new BookResponse.AuthorDTO(
                        book.getAuthor().getId(),
                        book.getAuthor().getName()
                ))
                .price(book.getPrice())
                .shelf(new BookResponse.ShelfDTO(
                        book.getShelf().getId(),
                        book.getShelf().getCode(),
                        book.getShelf().getLocation()
                ))
                .isAvailable(book.getBookItems() != null ? book.getBookItems().stream().anyMatch(BookItem::isAvailable) : null)
                .categories(categories)
                .build();
    }
}
