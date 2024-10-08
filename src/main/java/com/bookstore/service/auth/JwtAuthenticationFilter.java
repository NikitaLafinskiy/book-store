package com.bookstore.service.auth;

import com.bookstore.service.token.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null && jwtUtil.isValid(token)) {
            String subject = jwtUtil.getSubject(token);
            Collection<? extends GrantedAuthority> authorities = jwtUtil.getAuthorities(token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    subject,
                    null,
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            authHeader = authHeader.trim();
            String[] authHeaderParts = authHeader.split(" ");
            if (authHeaderParts.length == 2) {
                String authPrefix = authHeaderParts[0];
                String authValue = authHeaderParts[1];
                if (authPrefix.trim().equalsIgnoreCase("Bearer")) {
                    return authValue;
                }
            }
        }

        return null;
    }
}
