package tictactoe.domain.service;

import tictactoe.datasource.mapper.UserMapper;
import tictactoe.datasource.repository.UserRepository;
import tictactoe.domain.model.User.Role;
import tictactoe.domain.model.User;

import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(String login, String password) {
        User user = new User(UUID.randomUUID(), login, password, List.of(Role.USER));
        repository.save(UserMapper.toEntity(user));
        return user;
    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login).map(UserMapper::toDomain).orElse(null);
    }

    @Override
    public User findById(UUID id) {
        return repository.findById(id).map(UserMapper::toDomain).orElse(null);
    }
}
