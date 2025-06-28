package com.library.userservice.controller;

import com.library.commonservice.dto.response.UserResponse;
import com.library.userservice.dto.UserRequest;
import com.library.userservice.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class InternalUserController {
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request,
            @RequestHeader("X-Internal-Api-Key") String apiKeyHeader){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUserInternal(apiKeyHeader, request));
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email, @RequestHeader("X-Internal-Api-Key") String apiKeyHeader){
        return ResponseEntity.ok().body(userService.getUserByEmailInternal(apiKeyHeader, email));
    }
}
