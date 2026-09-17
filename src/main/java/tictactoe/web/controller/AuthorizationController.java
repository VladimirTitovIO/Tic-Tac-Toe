package tictactoe.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tictactoe.domain.model.User;
import tictactoe.domain.service.UserService;
import tictactoe.web.model.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthorizationService authService;

    public AuthorizationController(AuthorizationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpRequest request) {
        boolean success = authService.register(request);
        if (!success) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        return ResponseEntity.ok("New user registered");
    }

    @GetMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
        JwtResponse response = authService.authorize(jwtRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-access")
    public ResponseEntity<JwtResponse> updateAccessToken(@RequestBody RefreshJwtRequest refreshRequest) {
        JwtResponse response = authService.refreshAccessToken(refreshRequest.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-refresh")
    public ResponseEntity<JwtResponse> updateRefreshToken(@RequestBody RefreshJwtRequest refreshRequest) {
        JwtResponse response = authService.refreshRefreshToken(refreshRequest.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
