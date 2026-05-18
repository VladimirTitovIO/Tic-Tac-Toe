package tictactoe.datasource.mapper;

import tictactoe.datasource.model.UserEntity;
import tictactoe.domain.model.User;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getLogin(), user.getPassword());
    }

    public static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getLogin(), entity.getPassword());
    }
}
