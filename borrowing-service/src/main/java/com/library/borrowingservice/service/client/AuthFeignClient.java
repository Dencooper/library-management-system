package com.library.borrowingservice.service.client;

import com.library.commonservice.dto.ApiResponse;
import com.library.commonservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("AUTH-SERVICE")
public interface AuthFeignClient {
    @GetMapping(value = "/api/v1/auth/user", consumes = "application/json")
    public ResponseEntity<ApiResponse<UserResponse>> fetchUser();
}
