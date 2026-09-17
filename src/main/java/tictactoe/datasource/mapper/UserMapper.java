package tictactoe.datasource.mapper;

import tictactoe.datasource.model.UserEntity;
import tictactoe.domain.model.User.Role;
import tictactoe.domain.model.User;

import java.util.List;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity(user.getId(), user.getLogin(), user.getPassword());
        entity.setRoles(user.getRoles());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        return new User(entity. getId(), entity.getLogin(), entity.getPassword(), entity.getRoles());
    }
}
