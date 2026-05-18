package tictactoe.web.mapper;

import org.junit.jupiter.api.Test;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameBoard;
import tictactoe.web.model.GameWebModel;
import tictactoe.web.model.GameBoardWebModel;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameWebMapperTest {
    @Test
    void shouldMapDomainToWeb() {
        UUID gameId = UUID.randomUUID();
        Game domain = new Game(gameId, new GameBoard());
        domain.setVsComputer(true);
        GameWebModel web = GameWebMapper.toWeb(domain);

        assertNotNull(web);
        assertEquals(gameId, web.getId());
        assertEquals(domain.isVsComputer(), web.isVsComputer());
    }

    @Test
    void shouldMapWebToDomain() {
        UUID gameId = UUID.randomUUID();
        GameWebModel web = new GameWebModel(gameId, new GameBoardWebModel(new int[3][3]), true);
        Game domain = GameWebMapper.toDomain(web);

        assertNotNull(domain);
        assertEquals(gameId, domain.getId());
        assertEquals(web.isVsComputer(), domain.isVsComputer());
    }

    @Test
    void shouldThrowExceptionForNullWeb() {
        assertThrows(IllegalArgumentException.class, () -> GameWebMapper.toDomain(null));
    }

    @Test
    void shouldThrowExceptionForNullDomain() {
        assertThrows(IllegalArgumentException.class, () -> GameWebMapper.toWeb(null));
    }
}
