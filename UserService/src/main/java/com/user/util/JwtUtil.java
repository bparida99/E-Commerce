package com.user.util;

import com.user.exception.InvalidTokenException;
import com.user.service.impl.UserAuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    private static final String SECRETS = "my-secret-key-for-jwt-token-generation-#12345567";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRETS.getBytes(StandardCharsets.UTF_8));
    private final UserAuthenticationService userService;

    public JwtUtil(UserAuthenticationService userService) {
        this.userService = userService;
    }

    public String generateToken(String email) {
        return Jwts.builder().setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public UsernamePasswordAuthenticationToken extractUserName(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String userName = claims.getSubject();
        if (userName != null && userName.isBlank()) {
            throw new InvalidTokenException("Please provide a valid token");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new InvalidTokenException("Token has been expired.");
        }
        UserDetails userDetails = userService.loadUserByUsername(userName);
        if (!userDetails.getUsername().equals(userName)) {
            throw new InvalidTokenException("Please provide a valid token");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}
