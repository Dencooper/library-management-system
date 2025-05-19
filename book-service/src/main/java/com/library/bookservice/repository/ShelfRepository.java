package com.library.bookservice.repository;

import com.library.bookservice.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    List<Shelf> findAllByIsDeletedFalse();

    Optional<Shelf> findByIdAndIsDeletedFalse(Long id);
}
