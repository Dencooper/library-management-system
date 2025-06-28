package com.library.bookservice.repository;

import com.library.bookservice.model.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookItemRepository extends JpaRepository<BookItem, Long> {
    List<BookItem> findByBookIdAndIsAvailable(Long bookId, Boolean isAvailable);
    List<BookItem> findByBookId(Long bookId);
    Optional<BookItem> findByCodeAndIsAvailableTrue(String code);
    Optional<BookItem> findByCode(String code);
    boolean existsByCode(String code);
    List<BookItem> findByIsDeletedFalse();
    List<BookItem> findByIsDeletedFalseAndIsAvailable(Boolean isAvailable);
}
