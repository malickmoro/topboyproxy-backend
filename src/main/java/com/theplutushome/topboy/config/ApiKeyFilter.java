package com.theplutushome.topboy.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
@PropertySource("classpath:application.yml")
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);
    private static final String API_KEY_HEADER = "X-API-KEY";
    private final String expectedApiKey;

    // Public routes (no API key)
    private static final List<String> PUBLIC_PREFIXES = List.of(
        "/admin/login",
        "/admin/create-user",
        "/api/client/redde/callback",
        "/api/client/hubtel/callback",
        "/swagger-ui",
        "/v3/api-docs",
        "/swagger-resources",
        "/webjars"
    );

    public ApiKeyFilter(Environment env) {
        String rawKey = env.getProperty("application.api.key");
        if (rawKey == null || rawKey.isBlank()) {
            throw new IllegalStateException("API key must be configured via application.api.key");
        }
        this.expectedApiKey = rawKey.trim();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // Skip anything under the public prefixes
        boolean isPublic = PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
        if (isPublic) return true;

        // Enforce API key **only** for /api/*
        boolean isApi = path.startsWith("/api/");
        return !isApi;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        final String provided = req.getHeader(API_KEY_HEADER);
        if (provided == null || !expectedApiKey.equals(provided.trim())) {
            log.warn("ApiKeyFilter: invalid/missing X-API-KEY on {}", req.getRequestURI());
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"invalid_api_key\"}");
            return;
        }

        // Optionally set a ROLE for downstream @PreAuthorize checks
        var auth = new UsernamePasswordAuthenticationToken(
            "apiClient", null, List.of(new SimpleGrantedAuthority("ROLE_API")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(req, res);
    }
}
