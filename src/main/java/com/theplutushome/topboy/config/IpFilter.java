package com.theplutushome.topboy.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IpFilter extends OncePerRequestFilter {

    private static final String ALLOWED_IP = "108.129.40.25";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        if (!ALLOWED_IP.equals(clientIp)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid IP Address");
            return;
        }
        filterChain.doFilter(request, response);
    }
}