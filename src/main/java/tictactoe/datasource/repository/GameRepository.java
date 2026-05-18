package tictactoe.datasource.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tictactoe.datasource.model.GameEntity;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {
}
