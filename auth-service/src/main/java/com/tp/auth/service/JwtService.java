package com.tp.auth.service;

import com.tp.auth.config.AuthProperties;
import com.tp.auth.entity.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Issues short-lived HS256 access tokens. Subject = username; extra claims carry
 * the role and optional studentId. The gateway validates these using the same
 * app.jwt.secret.
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long accessExpirationMs;

    public JwtService(AuthProperties properties) {
        this.key = Keys.hmacShaKeyFor(properties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = properties.getJwt().getAccessExpirationMs();
    }

    public String generateToken(AppUser user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpirationMs);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .claim("studentId", user.getStudentId())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
