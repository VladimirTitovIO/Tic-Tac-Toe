package tictactoe.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import tictactoe.domain.model.Game.GameStates;
import tictactoe.domain.model.Game.Token;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    void shouldCreateGameWithEmptyBoard() {
        UUID id = UUID.randomUUID();
        GameBoard board = new GameBoard();

        Game game = new Game(id, board);

        assertEquals(id, game.getId());
        assertEquals(board, game.getGameBoard());
        assertNotNull(game.getGameBoard());
        assertFalse(game.isVsComputer());
    }

    @Test
    void shouldSetAndGetPlayers() {
        Game game = new Game(UUID.randomUUID(), new GameBoard());
        UUID playerX = UUID.randomUUID();
        UUID playerO = UUID.randomUUID();

        game.setPlayerXId(playerX);
        game.setPlayerOId(playerO);

        assertEquals(playerX, game.getPlayerXId());
        assertEquals(playerO, game.getPlayerOId());
    }

    @Test
    void shouldSetAndGetGameState() {
        Game game = new Game(UUID.randomUUID(), new GameBoard());

        game.setState(GameStates.IN_PROGRESS);

        assertEquals(GameStates.IN_PROGRESS, game.getState());
    }

    @Test
    void shouldSetAndGetVsComputer() {
        Game game = new Game(UUID.randomUUID(), new GameBoard());

        game.setVsComputer(true);

        assertTrue(game.isVsComputer());
    }

    @Test
    void shouldSetAndGetCurrentTurn() {
        Game game = new Game(UUID.randomUUID(), new GameBoard());
        UUID playerId = UUID.randomUUID();

        game.setCurrentTurn(playerId);

        assertEquals(playerId, game.getCurrentTurn());
    }

    @Test
    void tokenShouldHaveCorrectValue() {
        assertEquals(1, Game.Token.X.getValue());
        assertEquals(-1, Game.Token.O.getValue());
    }

    @Test
    void tokenShouldGetCorrectValueFromBoard() {
        GameBoard board = new GameBoard();

        board.setValue(0, 0, -1);
        board.setValue(0, 2, 1);

        assertEquals(Token.O.getValue(), board.getValue(0, 0));
        assertEquals(Token.X.getValue(), board.getValue(0, 2));
    }

}
