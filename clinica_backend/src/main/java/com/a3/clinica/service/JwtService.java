package com.a3.clinica.service;

import com.a3.clinica.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class JwtService {
    // Generate a secure key that's at least 256 bits (32 bytes)
    private final SecretKey key;

    public JwtService() {
        // You should move this to your application.properties/application.yml
        // and inject it via @Value annotation in a production environment
        String secretKey = generateSecureKey();
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    private String generateSecureKey() {
        // Generate a secure random 32-byte (256-bit) key
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(key)  // SignatureAlgorithm.HS256 is now the default
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()  // Using the new fluent API
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseToken(String token) {
        return Jwts.parser()  // Using the new fluent API
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}