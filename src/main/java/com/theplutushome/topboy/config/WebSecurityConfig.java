package com.theplutushome.topboy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final ApiKeyFilter apiKeyFilter;
    private final IpFilter ipFilter;

    @Autowired
    public WebSecurityConfig(JwtFilter jwtFilter, ApiKeyFilter apiKeyFilter, IpFilter ipFilter) {
        this.jwtFilter = jwtFilter;
        this.apiKeyFilter = apiKeyFilter;
        this.ipFilter = ipFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(
                    "/admin/login",
                    "/admin/create-user",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/api/verify-captcha",
                    "/api/client/hubtel/callback",
                    "/api/client/redde/callback"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(ipFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(apiKeyFilter, JwtFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "https://admin.topboyproxy.com",
            "https://topboyproxy.com",
            "https://www.topboyproxy.com",
            "https://topboyproxy-client-test.vercel.app",
            "http://localhost:3000"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        return request -> config;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
