package tictactoe.web.model;

import io.jsonwebtoken.Claims;
import tictactoe.domain.model.User;
import tictactoe.domain.service.UserService;

import java.util.Base64;
import java.util.UUID;

public class DefaultAuthorizationService implements AuthorizationService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private JwtUtil jwtUtil;

    public DefaultAuthorizationService(UserService userService, JwtProvider jwtProvider, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean register(SignUpRequest request) {
        if (userService.findByLogin(request.getLogin()) != null) {
            return false;
        }
        userService.createUser(request.getLogin(), request.getPassword());
        return true;
    }

    @Override
    public JwtResponse authorize(JwtRequest jwtRequest) {
        String login = jwtRequest.getLogin();
        String password = jwtRequest.getPassword();
        User user = userService.findByLogin(login);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalStateException("Invalid login or password");
        }
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshAccessToken(String refreshToken) {
        User user = getUserFromRefreshToken(refreshToken);
        String accessToken = jwtProvider.generateAccessToken(user);
        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshRefreshToken(String refreshToken) {
        User user = getUserFromRefreshToken(refreshToken);
        String accessToken = jwtProvider.generateAccessToken(user);
        refreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    @Override
    public User getUserFromRefreshToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        Claims claims = jwtProvider.getClaims(refreshToken);
        UUID userId = UUID.fromString(claims.get("userUUID", String.class));
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    @Override
    public JwtAuthentication getJwtAuthentication(String accessToken) {
        Claims claims = jwtProvider.getClaims(accessToken);
        return jwtUtil.createJwtAuthenticationFromClaims(claims);
    }
}