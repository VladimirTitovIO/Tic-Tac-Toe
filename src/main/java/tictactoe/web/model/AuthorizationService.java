package tictactoe.web.model;

import tictactoe.domain.model.User;

import java.util.UUID;

public interface AuthorizationService {
    boolean register(SignUpRequest request);
    JwtResponse authorize(JwtRequest jwtRequest);
    public JwtResponse refreshAccessToken(String refreshToken);
    public JwtResponse refreshRefreshToken(String refreshToken);
    public User getUserFromRefreshToken(String refreshToken);
    public JwtAuthentication getJwtAuthentication(String accessToken);
}
