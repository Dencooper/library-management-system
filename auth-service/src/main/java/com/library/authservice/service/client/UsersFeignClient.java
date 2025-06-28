package com.library.authservice.service.client;

import com.library.authservice.dto.request.UserCreationRequest;
import com.library.commonservice.dto.ApiResponse;
import com.library.commonservice.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("USER-SERVICE")
public interface UsersFeignClient {
    @GetMapping(value = "/api/v1/internal/users/email", consumes = "application/json")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(
            @RequestParam String email,
            @RequestHeader("X-Internal-Api-Key") String internalKey);

    @PostMapping(value = "/api/v1/internal/users", consumes = "application/json")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserCreationRequest request,
            @RequestHeader("X-Internal-Api-Key") String internalKey);
}
