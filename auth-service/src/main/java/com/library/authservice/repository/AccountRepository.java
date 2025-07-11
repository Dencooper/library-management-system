package com.library.authservice.repository;

import com.library.authservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByRefreshTokenAndEmail(String token, String email);
}
