package com.tp.auth.controller;

import com.tp.auth.config.AuthProperties;
import com.tp.auth.dto.AuthResponse;
import com.tp.auth.dto.LoginRequest;
import com.tp.auth.dto.RegisterRequest;
import com.tp.auth.entity.AppUser;
import com.tp.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * Authentication endpoints. The refresh token is delivered only as an httpOnly
 * cookie scoped to /api/auth; the access token is returned in the JSON body.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String REFRESH_COOKIE = "refreshToken";
    private static final String COOKIE_PATH = "/api/auth";

    private final AuthService authService;
    private final AuthProperties properties;

    public AuthController(AuthService authService, AuthProperties properties) {
        this.authService = authService;
        this.properties = properties;
    }

    // POST /api/auth/register — create an ADMIN user
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AppUser user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(null, user.getUsername(), user.getRole().name()));
    }

    // POST /api/auth/login — validate creds, return access token + set refresh cookie
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthService.AuthResult result = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(result.refreshToken()).toString())
                .body(result.response());
    }

    // POST /api/auth/refresh — rotate refresh cookie, return a new access token
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken) {
        AuthService.AuthResult result = authService.refresh(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(result.refreshToken()).toString())
                .body(result.response());
    }

    // POST /api/auth/logout — revoke refresh token and clear the cookie
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearRefreshCookie().toString())
                .build();
    }

    private ResponseCookie buildRefreshCookie(String value) {
        return ResponseCookie.from(REFRESH_COOKIE, value)
                .httpOnly(true)
                .secure(properties.getCookie().isSecure())
                .path(COOKIE_PATH)
                .sameSite("Lax")
                .maxAge(Duration.ofDays(properties.getRefresh().getExpirationDays()))
                .build();
    }

    private ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true)
                .secure(properties.getCookie().isSecure())
                .path(COOKIE_PATH)
                .sameSite("Lax")
                .maxAge(0)
                .build();
    }
}
