package com.tp.auth.service;

import com.tp.auth.config.AuthProperties;
import com.tp.auth.entity.RefreshToken;
import com.tp.auth.exception.InvalidRefreshTokenException;
import com.tp.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Manages refresh tokens. The raw token is returned to the caller once (to be
 * set as an httpOnly cookie); only its SHA-256 hash is persisted. Rotation
 * deletes the presented token and issues a new one.
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final long expirationDays;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository repository, AuthProperties properties) {
        this.repository = repository;
        this.expirationDays = properties.getRefresh().getExpirationDays();
    }

    /** Create a new refresh token for a user; returns the raw value (store the hash). */
    @Transactional
    public String createToken(String username) {
        String rawToken = generateRawToken();
        RefreshToken entity = new RefreshToken(
                sha256(rawToken),
                username,
                Instant.now().plus(expirationDays, ChronoUnit.DAYS)
        );
        repository.save(entity);
        return rawToken;
    }

    /**
     * Validate the presented raw token, delete it (rotation), and issue a fresh
     * one for the same user. Throws if unknown or expired.
     */
    @Transactional
    public Rotation verifyAndRotate(String rawToken) {
        RefreshToken existing = repository.findByTokenHash(sha256(rawToken))
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token is invalid"));

        if (existing.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(existing);
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }

        repository.delete(existing);
        String newRaw = createToken(existing.getUsername());
        return new Rotation(existing.getUsername(), newRaw);
    }

    /** Revoke a refresh token if present (no-op if unknown). */
    @Transactional
    public void revoke(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }
        repository.deleteByTokenHash(sha256(rawToken));
    }

    private String generateRawToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /** Result of a rotation: the owner and the new raw refresh token. */
    public record Rotation(String username, String rawToken) {}
}
