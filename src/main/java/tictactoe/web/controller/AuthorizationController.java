package tictactoe.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tictactoe.web.model.AuthorizationService;
import tictactoe.web.model.SignUpRequest;

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
    public ResponseEntity<?> login(@RequestHeader("Authorization") String header) {
        String base64 = header.replace("Basic ", "");
        UUID id = authService.authorize(base64);
        return ResponseEntity.ok(id);
    }


}
