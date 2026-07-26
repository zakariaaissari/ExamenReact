package com.tp.gateway.filter;

import com.tp.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * The single point where access tokens are validated. Runs for every request:
 *   - requests to /api/auth/** pass through untouched (login/refresh are public),
 *   - all others must carry a valid "Authorization: Bearer <jwt>" (HS256),
 *   - on success, the username and role are injected as X-Auth-User / X-Auth-Role
 *     so downstream services never parse the token themselves,
 *   - missing/invalid tokens get a 401 and never reach a downstream service.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTH_PREFIX = "/api/auth";
    private static final String BEARER = "Bearer ";

    private final SecretKey key;

    public JwtAuthenticationFilter(JwtProperties properties) {
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Public auth endpoints — no token required.
        if (path.startsWith(AUTH_PREFIX)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return unauthorized(exchange, "Missing or malformed Authorization header");
        }

        String token = authHeader.substring(BEARER.length());
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            Object role = claims.get("role");

            ServerHttpRequest mutated = request.mutate()
                    .header("X-Auth-User", username != null ? username : "")
                    .header("X-Auth-Role", role != null ? role.toString() : "")
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build());
        } catch (JwtException | IllegalArgumentException e) {
            return unauthorized(exchange, "Invalid or expired token");
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String reason) {
        log.warn("Rejected request to {}: {}", exchange.getRequest().getURI().getPath(), reason);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    /**
     * Order -1: runs after the logging filter (HIGHEST_PRECEDENCE) but before the
     * gateway's routing filters, so unauthenticated requests are stopped early.
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
