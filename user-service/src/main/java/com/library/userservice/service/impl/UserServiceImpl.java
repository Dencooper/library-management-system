package com.library.userservice.service.impl;

import com.library.userservice.dto.RegisterRequest;
import com.library.userservice.dto.UserRequest;
import com.library.userservice.dto.UserResponse;
import com.library.userservice.mapper.UserMapper;
import com.library.userservice.model.User;
import com.library.userservice.repository.UserRepository;
import com.library.userservice.service.IUserService;
import com.library.userservice.service.client.AuthFeignClient;
import com.library.userservice.utils.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthFeignClient authFeignClient;

    @Value("${library.internal-api-key}")
    private String apiKey;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(UserRequest request) {
        if (request.getRole().equals(Role.LIBRARIAN)){
            RegisterRequest registerRequest = RegisterRequest.builder()
                    .email(request.getEmail())
                    .password("Librarian@123")
                    .fullName(request.getFullName())
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .build();
            authFeignClient.addAccount(registerRequest);
        }
        User user = userMapper.toEntity(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse createUserInternal(String apiKeyHeader, UserRequest request) {
        if (!apiKey.equals(apiKeyHeader)) {
            throw new RuntimeException("Forbidden");
        }
        User user = userMapper.toEntity(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("User not found or already deleted"));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found or already deleted"));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmailInternal(String apiKeyHeader, String email) {
        if (!apiKey.equals(apiKeyHeader)) {
            throw new RuntimeException("Forbidden");
        }
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found or already deleted"));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllByIsDeletedFalse()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("User not found or already deleted"));
        user = userMapper.updateUser(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Void updateDeleteStatus(Long id, boolean isDeleted) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("User not found or already deleted"));
        user.setDeleted(true);
        userRepository.save(user);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserQuantity() {
        return userRepository.count();
    }
}
