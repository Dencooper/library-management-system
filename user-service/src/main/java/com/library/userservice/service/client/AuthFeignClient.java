package com.library.userservice.service.client;

import com.library.commonservice.dto.ApiResponse;
import com.library.userservice.dto.RegisterRequest;
import com.library.userservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("AUTH-SERVICE")
public interface AuthFeignClient {
    @PostMapping(value = "/api/v1/auth", consumes = "application/json")
    public ResponseEntity<ApiResponse<UserResponse>> addAccount(@RequestBody RegisterRequest request);
}
