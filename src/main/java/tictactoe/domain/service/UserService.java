package tictactoe.domain.service;

import tictactoe.domain.model.User;

import java.util.UUID;

public interface UserService {
    User createUser(String login, String password);
    User findByLogin(String login);
    User findById(UUID id);
}
