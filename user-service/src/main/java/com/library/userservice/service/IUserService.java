package com.library.userservice.service;

import com.library.userservice.dto.UserRequest;
import com.library.userservice.dto.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserRequest request);
    UserResponse createUserInternal(String apiKeyHeader, UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    UserResponse getUserByEmailInternal(String apiKeyHeader, String email);
    List<UserResponse> getAllUsers();
    Void updateDeleteStatus(Long id, boolean isDeleted);

}
