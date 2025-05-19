package com.library.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.library.authservice.constant.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {

    @JsonProperty("access_token")
    String accessToken;
    AccountInnerToken account;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AccountInnerToken {
        Long id;
        String email;
    }
}
