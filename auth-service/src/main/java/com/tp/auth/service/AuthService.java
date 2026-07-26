package com.tp.auth.service;

import com.tp.auth.dto.AuthResponse;
import com.tp.auth.dto.LoginRequest;
import com.tp.auth.dto.RegisterRequest;
import com.tp.auth.entity.AppUser;
import com.tp.auth.entity.Role;
import com.tp.auth.exception.InvalidCredentialsException;
import com.tp.auth.exception.InvalidRefreshTokenException;
import com.tp.auth.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Orchestrates registration, login, refresh and logout by combining the user
 * store, JWT issuing and refresh-token rotation. Keeps the controller thin.
 */
@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AppUserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public AppUser register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }
        AppUser user = new AppUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Role.ADMIN,
                request.getStudentId()
        );
        return userRepository.save(user);
    }

    public AuthResult login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createToken(user.getUsername());
        return new AuthResult(
                new AuthResponse(accessToken, user.getUsername(), user.getRole().name()),
                refreshToken
        );
    }

    public AuthResult refresh(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new InvalidRefreshTokenException("Missing refresh token");
        }
        RefreshTokenService.Rotation rotation = refreshTokenService.verifyAndRotate(rawRefreshToken);

        AppUser user = userRepository.findByUsername(rotation.username())
                .orElseThrow(() -> new InvalidRefreshTokenException("User no longer exists"));

        String accessToken = jwtService.generateToken(user);
        return new AuthResult(
                new AuthResponse(accessToken, user.getUsername(), user.getRole().name()),
                rotation.rawToken()
        );
    }

    public void logout(String rawRefreshToken) {
        refreshTokenService.revoke(rawRefreshToken);
    }

    /** Access-token response plus the raw refresh token to be set as a cookie. */
    public record AuthResult(AuthResponse response, String refreshToken) {}
}
