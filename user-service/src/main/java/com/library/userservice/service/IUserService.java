package com.library.userservice.service;

import com.library.commonservice.dto.response.UserResponse;
import com.library.userservice.dto.UserRequest;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserRequest request);
    UserResponse createUserInternal(String apiKeyHeader, UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    UserResponse getUserByEmailInternal(String apiKeyHeader, String email);
    UserResponse getUserByIdInternal(String apiKeyHeader, Long id);
    List<UserResponse> getAllUsers();
    Void updateDeleteStatus(Long id, boolean isDeleted);
    Long getUserQuantity();
    Void updateBannedUser(Long id, Boolean isBanned);
}
