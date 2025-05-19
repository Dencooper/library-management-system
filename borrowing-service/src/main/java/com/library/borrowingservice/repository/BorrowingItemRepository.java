package com.library.borrowingservice.repository;

import com.library.borrowingservice.model.BorrowingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingItemRepository extends JpaRepository<BorrowingItem, Long> {
    List<BorrowingItem> findByBorrowingId(Long borrowingId);
}
