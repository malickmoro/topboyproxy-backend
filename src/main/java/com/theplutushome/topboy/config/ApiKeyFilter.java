// ApiKeyFilter.java
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
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
@PropertySource("classpath:application.yml")
public class ApiKeyFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);
    private static final String API_KEY_HEADER = "X-API-KEY";
    private final String EXPECTED_API_KEY; // Replace with your API key

    // Inject the Environment and retrieve the API key
    public ApiKeyFilter(Environment environment) {
        String apiKey = environment.getProperty("application.api.key");
        if (apiKey == null) {
            throw new IllegalStateException("API key must be configured via application.api.key");
        }
        this.EXPECTED_API_KEY = apiKey;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("ApiKeyFilter: doFilterInternal");

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Attempt API Key validation
        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey != null) {

            log.warn("Provided key chars: " + apiKey.chars().boxed().toList());
            log.warn("Expected key chars: " + EXPECTED_API_KEY.chars().boxed().toList());
            
            if (EXPECTED_API_KEY.equals(apiKey)) {
                // Set authentication with ROLE_API
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        "apiClient", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_API")));
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("ApiKeyFilter: Authentication set for ROLE_API");
                filterChain.doFilter(request, response);
                return;
            } else {
                // Invalid API Key provided
                log.warn("ApiKeyFilter: Invalid API Key provided ----> " + apiKey + " the expected key is --> " + EXPECTED_API_KEY);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden: Invalid API Key");
                return;
            }
        }

        // API Key is absent, allow other filters (like JwtFilter) to handle
        // authentication
        filterChain.doFilter(request, response);
    }
}
