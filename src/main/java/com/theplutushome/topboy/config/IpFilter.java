package com.theplutushome.topboy.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class IpFilter extends OncePerRequestFilter {

    private final Map<RequestMatcher, String> allowedIpPerEndpoint;

    public IpFilter() {
        this.allowedIpPerEndpoint = Map.of(
            // Modern constructor with HTTP method (optional)
            AntPathRequestMatcher.antMatcher("/api/client/callback"), "108.129.40.25"
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        if (clientIp == null || clientIp.isEmpty()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid IP Address");
            return;
        }

        for (Map.Entry<RequestMatcher, String> entry : allowedIpPerEndpoint.entrySet()) {
            if (entry.getKey().matches(request)) {
                if (!entry.getValue().equals(clientIp)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid IP Address");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
