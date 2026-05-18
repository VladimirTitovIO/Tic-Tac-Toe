package tictactoe.web.model;

import java.util.UUID;

public interface AuthorizationService {
    boolean register(SignUpRequest request);
    UUID authorize(String base64);
}
