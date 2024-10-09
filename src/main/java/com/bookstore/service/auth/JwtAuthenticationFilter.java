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
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";
    private static final int AUTH_HEADER_PARTS_LENGTH = 2;
    private static final String AUTH_HEADER_DELIMITER = " ";
    private static final int BEARER_INDEX = 0;
    private static final int TOKEN_INDEX = 1;
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
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null) {
            authHeader = authHeader.trim();
            String[] authHeaderParts = authHeader.split(AUTH_HEADER_DELIMITER);
            if (authHeaderParts.length == AUTH_HEADER_PARTS_LENGTH) {
                String authPrefix = authHeaderParts[BEARER_INDEX];
                String authValue = authHeaderParts[TOKEN_INDEX];
                if (authPrefix.trim().equalsIgnoreCase(BEARER_PREFIX)) {
                    return authValue;
                }
            }
        }

        return null;
    }
}
