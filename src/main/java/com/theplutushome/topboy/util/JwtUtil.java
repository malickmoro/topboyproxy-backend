package com.theplutushome.topboy.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;

@PropertySource("classpath:application.yml")
@Service
@RequiredArgsConstructor
public class JwtUtil {

    private final String SECRET;
    private final SecretKey SIGNING_KEY;

    @Autowired
    public JwtUtil(Environment env) {
        SECRET = env.getProperty("jwt.secret.key");
        assert SECRET != null;
        SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username) {
        long EXPIRATION_TIME = 60_000 * 60 * 24;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY)
                .compact();
    }

    public Claims extractClaim(String token) throws ExpiredJwtException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaim(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

    public boolean tokenExpired(String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date());
    }

    public void verifyToken(String authHeader) {
        if (authHeader.isBlank()) {
            throw new JwtException("Missing authentication header");
        }

        String[] parts = authHeader.split(" ");

        if (parts.length < 2) {
            throw new JwtException("Missing token in header");
        }

        String token = parts[1];

        try {
            if (this.isTokenExpired(token)) {
                throw new ExpiredJwtException(null, null, "Token has expired");
            }
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid JWT structure: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new JwtException("Invalid or empty JWT: " + ex.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
                | IllegalArgumentException ex) {
            return false;
        }
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if ("JWT".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
