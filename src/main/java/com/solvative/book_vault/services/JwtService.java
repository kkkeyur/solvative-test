package com.solvative.book_vault.services;

import com.solvative.book_vault.entitites.auth.AuthUser;
import com.solvative.book_vault.security.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenProperties properties;

    private SecretKey key;
    private long expirationMs;



    public String generateToken(AuthUser user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole())
                .claim("memberId", user.getMemberId())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Long extractMemberId(String token) {
        return extractAllClaims(token).get("memberId", Long.class);
    }

    public boolean isTokenValid(String token, String username) {
        Claims claims = extractAllClaims(token);
        return username.equals(claims.getSubject())
                && claims.getExpiration().after(new Date());
    }
}