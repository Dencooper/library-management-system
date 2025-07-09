package com.library.userservice.repository;

import com.library.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsDeletedFalse(Long id);
    List<User> findAllByIsDeletedFalse();
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    Boolean existsByEmailAndIsDeletedFalse(String email);
}
