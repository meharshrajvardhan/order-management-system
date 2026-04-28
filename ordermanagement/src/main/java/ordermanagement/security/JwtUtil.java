package ordermanagement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import ordermanagement.security.JwtUtil;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Change this secret in production — must be 256-bit (32+ chars)
    private static final String SECRET = "orderManagementSecretKey1234567890AB";
    private static final long EXPIRATION_MS = 86400000; // 24 hours

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate token from username
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extract role from token
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    // Validate token
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}