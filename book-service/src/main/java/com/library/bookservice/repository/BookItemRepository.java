package com.library.bookservice.repository;

import com.library.bookservice.model.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookItemRepository extends JpaRepository<BookItem, Long> {
    List<BookItem> findByBookIdAndIsAvailable(Long bookId, Boolean isAvailable);
    List<BookItem> findByBookId(Long bookId);
    boolean existsByCode(String code);
    List<BookItem> findByIsDeletedFalse();
    List<BookItem> findByIsDeletedFalseAndIsAvailable(Boolean isAvailable);
}
