package com.library.authservice.service;

import com.library.authservice.dto.request.RegisterRequest;
import com.library.authservice.model.Account;
import com.library.commonservice.dto.response.UserResponse;

public interface IAuthService {
    UserResponse register(RegisterRequest request);
    Account fetchAccountByEmail(String email);
    UserResponse fetchUserByEmail(String email);
    Account updateRefreshToken(String email, String refreshToken);
    Account fetchAccountByRefreshTokenAndEmail(String token, String email);
    Account addAccount(RegisterRequest request);
}
