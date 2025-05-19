package com.library.borrowingservice.service.client;

import com.library.borrowingservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("USER-SERVICE")
public interface UsersFeignClient {
    @GetMapping(value = "/api/v1/users/{id}", consumes = "application/json")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id);

    @GetMapping(value = "/api/v1/users/email", consumes = "application/json")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email);
}
