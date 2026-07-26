package com.tp.auth.dto;

/**
 * Body returned on login/refresh: the access token and basic identity. The
 * refresh token is NOT here — it travels only in an httpOnly cookie.
 */
public class AuthResponse {

    private String token;
    private String username;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
