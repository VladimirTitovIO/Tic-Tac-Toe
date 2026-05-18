package tictactoe.datasource.repository;

import org.springframework.data.repository.CrudRepository;
import tictactoe.datasource.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    Optional<UserEntity> findByLogin(String login);
}
