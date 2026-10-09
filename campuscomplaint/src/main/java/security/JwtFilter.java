package com.smart.campuscomplaint.security;

import com.smart.campuscomplaint.entity.User;
import com.smart.campuscomplaint.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("REQUEST PATH = " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("JWT TOKEN NOT FOUND");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtUtil.validateToken(token)) {
                System.out.println("INVALID JWT TOKEN");
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid or expired token"
                );
                return;
            }

            String email = jwtUtil.extractEmail(token);

            System.out.println("JWT EMAIL = " + email);

            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                System.out.println("USER NOT FOUND");

                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "User not found"
                );
                return;
            }

            System.out.println("USER ROLE = " + user.getRole());

            String role = user.getRole();

            if (role == null || role.isBlank()) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "User role not assigned"
                );
                return;
            }

            // Example: ADMIN becomes ROLE_ADMIN
            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            null,
                            List.of(authority)
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            System.out.println("AUTHENTICATED EMAIL = " + user.getEmail());
            System.out.println("GRANTED AUTHORITY = " + authority.getAuthority());

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();

            System.out.println("JWT FILTER ERROR = " + e.getMessage());

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or expired token"
            );
        }
    }
}
