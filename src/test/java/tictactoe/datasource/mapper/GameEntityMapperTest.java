package tictactoe.datasource.mapper;

import org.junit.jupiter.api.Test;
import tictactoe.datasource.model.GameEntity;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameBoard;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameEntityMapperTest {
    @Test
    void shouldMapDomainToEntity() {
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, new GameBoard());
        game.setVsComputer(true);
        GameEntity entity = GameEntityMapper.toEntity(game);

        assertNotNull(entity);
        assertEquals(gameId, entity.getId());
        assertEquals(game.isVsComputer(), entity.isVsComputer());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID gameId = UUID.randomUUID();
        GameEntity entity = new GameEntity();
        entity.setId(gameId);
        entity.setBoard("0,0,0;0,0,0;0,0,0");
        Game game = GameEntityMapper.toDomain(entity);

        assertNotNull(game);
        assertNotNull(game.getGameBoard());
        assertEquals(game.getId(), entity.getId());
        assertArrayEquals(entity.convertStringToBoard(entity.getBoard()), game.getGameBoard().getBoard());
    }

    @Test
    void shouldThrowExceptionForNullEntity() {
        assertThrows(IllegalArgumentException.class, () -> GameEntityMapper.toDomain(null));
    }

    @Test
    void shouldThrowExceptionForNullDomain() {
        assertThrows(IllegalArgumentException.class, () -> GameEntityMapper.toEntity(null));
    }
}
