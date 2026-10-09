package com.smart.campuscomplaint.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(
                                "/auth/login",
                                "/register",
                                "/test",
                                "/h2-console/**"
                        ).permitAll()

                        // Only ADMIN can update complaints
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/complaints/**"
                        ).hasRole("ADMIN")

                        // Other endpoints require authentication
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // Prevent JwtFilter from being registered twice
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {

        FilterRegistrationBean<JwtFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(jwtFilter);
        registration.setEnabled(false);

        return registration;
    }
}
