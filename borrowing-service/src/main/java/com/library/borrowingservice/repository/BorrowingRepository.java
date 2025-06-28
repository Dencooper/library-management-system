package com.library.borrowingservice.repository;

import com.library.borrowingservice.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findAllByUserId(Long userId);

    List<Borrowing> findAllByLibrarianId(Long librarianId);

    List<Borrowing> findAllByReturnedAtIsNull();

}
