package tictactoe.web.model;

import tictactoe.domain.model.User;
import tictactoe.domain.service.UserService;

import java.util.Base64;
import java.util.UUID;

public class AuthorizationServiceImpl implements AuthorizationService {
    private final UserService userService;

    public AuthorizationServiceImpl(UserService userService) {
        this.userService = userService;
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
    public UUID authorize(String base64) {
        String decoded = new String(Base64.getDecoder().decode(base64));
        String[] parts = decoded.split(":");
        String login = parts[0];
        String password = parts[1];
        User user = userService.findByLogin(login);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalStateException("Invalid login or password");
        }
        return user.getId();
    }
}