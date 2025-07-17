package com.library.borrowingservice.repository;

import com.library.borrowingservice.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findAllByUserId(Long userId);

    List<Borrowing> findAllByReturnedAtIsNull();

    Boolean existsByUserIdAndReturnedAt(Long userId, LocalDateTime returnedAt);

    List<Borrowing> findAllByBorrowedAtBetween(LocalDateTime from, LocalDateTime to);
}
