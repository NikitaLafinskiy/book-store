package com.bookstore.service.token;

import com.bookstore.entity.User;
import com.bookstore.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String FIRST_NAME_CLAIM = "firstName";
    private static final String LAST_NAME_CLAIM = "lastName";
    private static final String SHIPPING_CLAIM = "shippingAddress";
    private final SecretKey secret;

    @Value("$jwt.expiration")
    private long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secretString) {
        secret = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generateToken(Authentication payload) {
        final long currentTime = System.currentTimeMillis();
        User principal = (User) payload.getPrincipal();

        return Jwts.builder()
                .issuedAt(new Date(currentTime))
                .expiration(new Date(currentTime + expiration))
                .subject(principal.getEmail())
                .claim(AUTHORITIES_CLAIM, payload.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .claim(SHIPPING_CLAIM, principal.getShippingAddress())
                .claim(FIRST_NAME_CLAIM, principal.getFirstName())
                .claim(LAST_NAME_CLAIM, principal.getLastName())
                .signWith(secret)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            final long currentTime = System.currentTimeMillis();
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);

            return claims.getPayload().getExpiration().after(new Date(currentTime));
        } catch (JwtException e) {
            throw new AuthenticationException("Invalid token", e);
        }
    }

    public String getSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        List<String> roles = (List<String>)
                getClaim(token, (claims) -> claims.get(AUTHORITIES_CLAIM));
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private <T> T getClaim(String token, Function<Claims, T> claimResolver) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claimResolver.apply(claims);
        } catch (JwtException e) {
            throw new RuntimeException("Failure processing the JWT token");
        }
    }
}
