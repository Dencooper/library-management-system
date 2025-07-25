package com.library.borrowingservice.service.client;

import com.library.commonservice.dto.ApiResponse;
import com.library.commonservice.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("USER-SERVICE")
public interface UsersFeignClient {
    @GetMapping(value = "/api/v1/users/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id);

    @GetMapping(value = "/api/v1/users/email", consumes = "application/json")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@RequestParam String email);

    @PutMapping("/api/v1/internal/users/{id}")
    public ResponseEntity<Void> updateBannedUser(@PathVariable Long id, @Valid @RequestParam Boolean isBanned);

    @GetMapping("/api/v1/internal/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByIdInternal(@PathVariable Long id, @RequestHeader("X-Internal-Api-Key") String apiKeyHeader);

    @GetMapping(value = "/api/v1/users", consumes = "application/json")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers();
}
