/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.theplutushome.topboy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author plutus
 */
@Configuration
@EnableMethodSecurity // enables @PreAuthorize, @PostAuthorize, etc.
public class WebSecurityConfig implements WebMvcConfigurer {

    private final JwtFilter jwtFilter;
    private final ApiKeyFilter apiKeyFilter;
    private final IpFilter ipFilter;

    public WebSecurityConfig(JwtFilter jwtFilter, ApiKeyFilter apiKeyFilter, IpFilter ipFilter) {
        this.jwtFilter = jwtFilter;
        this.apiKeyFilter = apiKeyFilter;
        this.ipFilter = ipFilter;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "https://admin.topboyproxy.com",
                "https://topboyproxy.com",        // add apex if you use it
                "https://www.topboyproxy.com"     // keep www if it’s real
            )
            .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true); // only if you need cookies/auth headers
    }

    // 1) Specific chain FIRST for payment callbacks (higher priority)
    @Bean
    @org.springframework.core.annotation.Order(1)
    SecurityFilterChain callbackChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                "/api/client/hubtel/callback",
                "/api/client/redde/callback"
            )
            .cors(withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            // Only attach the IP filter here so it runs ONLY for callbacks
            .addFilterBefore(ipFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // 2) Catch‑all chain LAST (lower priority)
    @Bean
    @org.springframework.core.annotation.Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
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
                    "/api/verify-captcha"
                    // NOTE: do NOT include the callback paths here;
                    // they’re handled by the callbackChain above.
                ).permitAll()
                .anyRequest().authenticated()
            )
            // Do NOT add ipFilter here
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(apiKeyFilter, JwtFilter.class)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
