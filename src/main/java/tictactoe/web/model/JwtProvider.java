package tictactoe.web.model;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tictactoe.domain.model.User.Role;
import tictactoe.domain.model.User;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secret}") String jwtSecret) {
            this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .claim("type", "access")
                .claim("userUUID", user.getId().toString())
                .claim("roles", user.getRoles().stream().map(Role::name).toList())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .claim("type", "refresh")
                .claim("userUUID", user.getId().toString())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Claims claims = getClaims(accessToken);
            return "access".equals(claims.get("type", String.class));
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = getClaims(refreshToken);
            return "refresh".equals(claims.get("type", String.class));
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
