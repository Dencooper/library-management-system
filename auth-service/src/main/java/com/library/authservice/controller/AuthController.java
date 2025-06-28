package com.library.authservice.controller;

import com.library.authservice.dto.request.LoginRequest;
import com.library.authservice.dto.request.RegisterRequest;
import com.library.authservice.dto.response.LoginResponse;
import com.library.authservice.dto.response.LoginResponse.AccountInnerToken;
import com.library.authservice.model.Account;
import com.library.authservice.service.IAuthService;
import com.library.authservice.utils.JwtUtil;
import com.library.commonservice.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final IAuthService iAuthService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;

    @Value("${library.jwt.refresh-token-validity-in-seconds}")
    long refreshTokenExpiration;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(iAuthService.register(request));
    }

    @PostMapping
    public ResponseEntity<Account> addAccount(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(iAuthService.addAccount(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        Account currrentAccount = iAuthService.fetchAccountByEmail(request.getEmail());

        LoginResponse res = new LoginResponse();
        AccountInnerToken accountInnerToken = new AccountInnerToken(
                currrentAccount.getId(),
                currrentAccount.getEmail()
        );

        res.setAccount(accountInnerToken);

        String access_token = jwtUtil.createAccessToken(authentication.getName(), currrentAccount);

        res.setAccessToken(access_token);

        String refresh_token = jwtUtil.createRefreshToken(request.getEmail(), res);
        iAuthService.updateRefreshToken(request.getEmail(), refresh_token);

        ResponseCookie resCookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> fetchUser() {
        String email = JwtUtil.getCurrentUserLogin().isPresent() ? JwtUtil.getCurrentUserLogin().get() : "";
        return ResponseEntity.ok().body(iAuthService.fetchUserByEmail(email));
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> fetchAccountByRefresh(
            @CookieValue(name = "refresh_token") String refresh_token) {
        Jwt decodedToken = jwtUtil.checkRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
        Account account = iAuthService.fetchAccountByRefreshTokenAndEmail(refresh_token, email);
        LoginResponse res = new LoginResponse();
        AccountInnerToken accountInnerToken = new AccountInnerToken(
                account.getId(),
                account.getEmail()
        );
        res.setAccount(accountInnerToken);

        String access_token = jwtUtil.createAccessToken(email, account);

        res.setAccessToken(access_token);

        String new_refresh_token = jwtUtil.createRefreshToken(email, res);
        iAuthService.updateRefreshToken(email, new_refresh_token);

        ResponseCookie resCookie = ResponseCookie.from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        String email = JwtUtil.getCurrentUserLogin().get();
        Account account = iAuthService.fetchAccountByEmail(email);
        account.setRefreshToken("");
        iAuthService.updateRefreshToken(email, null);
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(null);
    }
}
