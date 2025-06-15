package com.library.bookservice.repository;

import com.library.bookservice.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"author", "categories"})
    Optional<Book> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"author", "categories"})
    @Query("SELECT b FROM Book b")
    List<Book> findAllWithDetails();

    boolean existsByIsbn(String isbn);
}
