package com.library.authservice.service.impl;

import com.library.authservice.constant.Role;
import com.library.authservice.dto.request.RegisterRequest;
import com.library.authservice.dto.request.UserCreationRequest;
import com.library.authservice.model.Account;
import com.library.authservice.repository.AccountRepository;
import com.library.authservice.service.IAuthService;
import com.library.authservice.service.client.UsersFeignClient;
import com.library.commonservice.dto.response.UserResponse;
import com.library.commonservice.exception.AppException;
import com.library.commonservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final AccountRepository accountRepository;
    private final UsersFeignClient usersFeignClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${library.internal-api-key}")
    private String aipKey;

    @Override
    public UserResponse register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(Role.USER)
                .build();
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        accountRepository.save(account);
        return usersFeignClient.createUser(userCreationRequest, aipKey).getBody().getData();
    }

    @Override
    @PostAuthorize("hasRole('ADMIN')")
    public Account addAccount(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.LIBRARIAN)
                .build();
        return accountRepository.save(account);
    }

    @Override
    public Account fetchAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_EXISTED));
    }

    @Override
    public UserResponse fetchUserByEmail(String email) {
        return usersFeignClient.getUserByEmail(email, aipKey).getBody().getData();
    }

    @Override
    public Account updateRefreshToken(String email, String refreshToken) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_EXISTED));
        account.setRefreshToken(refreshToken);
        return accountRepository.save(account);
    }

    @Override
    public Account fetchAccountByRefreshTokenAndEmail(String token, String email) {
        return accountRepository.findByRefreshTokenAndEmail(token, email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
    }
}
