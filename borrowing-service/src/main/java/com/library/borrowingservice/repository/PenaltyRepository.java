package com.library.borrowingservice.repository;

import com.library.borrowingservice.model.Borrowing;
import com.library.borrowingservice.model.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
    List<Penalty> findAllByBorrowingIn(List<Borrowing> borrowings);
    List<Penalty> findAllByPenaltyAtBetween(LocalDateTime from, LocalDateTime to);
}
