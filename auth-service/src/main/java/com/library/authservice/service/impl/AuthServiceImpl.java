package com.library.authservice.service.impl;

import com.library.authservice.constant.Role;
import com.library.authservice.dto.request.RegisterRequest;
import com.library.authservice.dto.request.UserCreationRequest;
import com.library.authservice.dto.response.UserResponse;
import com.library.authservice.model.Account;
import com.library.authservice.repository.UserRepository;
import com.library.authservice.service.IAuthService;
import com.library.authservice.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final UsersFeignClient usersFeignClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${library.internal-api-key}")
    private String aipKey;

    @Override
    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User with email: " + request.getEmail() + " is existed");
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
                .enabled(true)
                .build();
        userRepository.save(account);
        return usersFeignClient.createUser(userCreationRequest,aipKey).getBody();
    }

    @Override
    @PostAuthorize("hasRole('ADMIN')")
    public Account addAccount(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User with email: " + request.getEmail() + " is existed");
        }

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.LIBRARIAN)
                .enabled(true)
                .build();
        return userRepository.save(account);
    }

    @Override
    public Account fetchAccountByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email: " + email + " is existed"));
    }

    @Override
    public UserResponse fetchUserByEmail(String email) {
        return usersFeignClient.getUserByEmail(email, aipKey).getBody();
    }

    @Override
    public Account updateRefreshToken(String email, String refreshToken) {
        Account account = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email: " + email + " is existed"));
        account.setRefreshToken(refreshToken);
        return userRepository.save(account);
    }

    @Override
    public Account fetchAccountByRefreshTokenAndEmail(String token, String email) {
        return userRepository.findByRefreshTokenAndEmail(token, email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
