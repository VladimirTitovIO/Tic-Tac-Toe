package tictactoe.web.model;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import tictactoe.domain.model.User;

import java.util.List;
import java.util.UUID;

@Component
public class JwtUtil {

    public JwtAuthentication createJwtAuthenticationFromClaims(Claims claims) {
        UUID userId = UUID.fromString(claims.get("userUUID", String.class));
        List<String> roleNames = claims.get("roles", List.class);
        List<User.Role> roles = roleNames.stream().map(User.Role::valueOf).toList();
        return new JwtAuthentication(userId, roles, true);
    }
}
