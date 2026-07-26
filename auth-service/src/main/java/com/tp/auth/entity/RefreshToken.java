package com.tp.auth.entity;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * A refresh token record. Only the SHA-256 hash of the raw token is stored, so
 * a database leak does not expose usable tokens. Rotation deletes the old row
 * and inserts a new one on each refresh.
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", unique = true, nullable = false)
    private String tokenHash;

    @Column(nullable = false)
    private String username;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    public RefreshToken() {}

    public RefreshToken(String tokenHash, String username, Instant expiryDate) {
        this.tokenHash = tokenHash;
        this.username = username;
        this.expiryDate = expiryDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Instant getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
}
