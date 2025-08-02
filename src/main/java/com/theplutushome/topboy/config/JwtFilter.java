package com.theplutushome.topboy.config;

import com.theplutushome.topboy.util.JwtUtil;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    private static final List<String> WHITELIST = List.of(
            "/admin/login",
            "/admin/create-user",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/swagger-ui/index.html",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/swagger-config",
            "/swagger-resources",
            "/swagger-resources/",
            "/webjars/"
    );

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return WHITELIST.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwt = extractJwtFromHeader(request);
        log.info("JwtFilter: doFilterInternal: JWT: {}", jwt);
        if (jwt != null && jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.extractClaim(jwt).getSubject();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("JwtFilter: doFilterInternal: Authentication set");
        } else {
            log.info("JwtFilter: doFilterInternal: Authentication not set");
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        log.info("JwtFilter: extractJwtFromHeader: Authorization={}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("JwtFilter: extractJwtFromHeader: Missing or invalid Authorization header");
            return null;
        }

        return authHeader.substring(7);
    }
}
