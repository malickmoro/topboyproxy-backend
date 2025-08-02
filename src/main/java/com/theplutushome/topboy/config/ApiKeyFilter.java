package com.theplutushome.topboy.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

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

    public ApiKeyFilter(Environment environment) {
        String rawKey = environment.getProperty("application.api.key");
        if (rawKey == null || rawKey.trim().isEmpty()) {
            throw new IllegalStateException("API key must be configured via application.api.key");
        }
        this.expectedApiKey = rawKey.trim();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String headerKey = request.getHeader(API_KEY_HEADER);
        if (headerKey != null) {
            String providedKey = headerKey.trim();

            if (expectedApiKey.equals(providedKey)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        "apiClient",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_API"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("API key authentication successful.");
            } else {
                log.warn("Invalid API Key provided. Provided: [{}] | Expected: [{}]", providedKey, expectedApiKey);
                log.warn("Provided chars: {}", providedKey.chars().boxed().toList());
                log.warn("Expected chars: {}", expectedApiKey.chars().boxed().toList());

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden: Invalid API Key");
                return;
            }
        }

        // Let other filters handle if no key was provided
        filterChain.doFilter(request, response);
    }
}
