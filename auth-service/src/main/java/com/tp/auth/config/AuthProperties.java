package com.tp.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Authentication configuration bound from the "app" prefix in application.yml:
 *   app.jwt.secret / app.jwt.access-expiration-ms
 *   app.refresh.expiration-days
 *   app.cookie.secure
 */
@ConfigurationProperties(prefix = "app")
public class AuthProperties {

    private Jwt jwt = new Jwt();
    private Refresh refresh = new Refresh();
    private Cookie cookie = new Cookie();

    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }

    public Refresh getRefresh() { return refresh; }
    public void setRefresh(Refresh refresh) { this.refresh = refresh; }

    public Cookie getCookie() { return cookie; }
    public void setCookie(Cookie cookie) { this.cookie = cookie; }

    public static class Jwt {
        private String secret;
        private long accessExpirationMs = 900_000L; // 15 min

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }

        public long getAccessExpirationMs() { return accessExpirationMs; }
        public void setAccessExpirationMs(long accessExpirationMs) { this.accessExpirationMs = accessExpirationMs; }
    }

    public static class Refresh {
        private long expirationDays = 7;

        public long getExpirationDays() { return expirationDays; }
        public void setExpirationDays(long expirationDays) { this.expirationDays = expirationDays; }
    }

    public static class Cookie {
        private boolean secure = false;

        public boolean isSecure() { return secure; }
        public void setSecure(boolean secure) { this.secure = secure; }
    }
}
