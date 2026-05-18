package tictactoe.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tictactoe.datasource.repository.GameRepository;
import tictactoe.datasource.model.GameEntity;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameBoard;
import tictactoe.domain.model.Game.GameStates;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameRepository repository;

    private GameService service;

    @BeforeEach
    void setUp() {
        service = new GameServiceImpl(repository);
    }

    @Test
    void shouldCreateGameVsPc() {
        UUID userId = UUID.randomUUID();
        when(repository.save(any(GameEntity.class))).thenReturn(null);
        Game game = service.createGame("PC", userId);

        assertNotNull(game.getId());
        assertTrue(game.isVsComputer());
        assertEquals(GameStates.IN_PROGRESS, game.getState());
        assertEquals(userId, game.getPlayerXId());
        assertEquals(userId, game.getCurrentTurn());
        verify(repository, times(1)).save(any(GameEntity.class));
    }

    @Test
    void shouldCreateGameVsPlayer() {
        UUID userId = UUID.randomUUID();
        when(repository.save(any(GameEntity.class))).thenReturn(null);
        Game game = service.createGame("Player", userId);

        assertNotNull(game.getId());
        assertFalse(game.isVsComputer());
        assertEquals(GameStates.WAITING_FOR_PLAYERS, game.getState());
        assertEquals(userId, game.getPlayerXId());
        verify(repository, times(1)).save(any(GameEntity.class));
    }

    @Test
    void shouldGetExistingGame() {
        UUID gameId = UUID.randomUUID();
        GameEntity entity = new GameEntity();
        entity.setId(gameId);
        entity.setBoard("0,0,0;0,0,0;0,0,0");
        entity.setVsComputer(true);
        when(repository.findById(gameId)).thenReturn(Optional.of(entity));
        Game game = service.getGame(gameId);

        assertNotNull(game);
        assertEquals(gameId, game.getId());
        verify(repository, times(1)).findById(gameId);
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        UUID gameId = UUID.randomUUID();
        when (repository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.getGame(gameId));
    }

    @Test
    void shouldDetectWinInRow() {
        int[][] win = {
                {1, 1, 1},
                {0, 0, 0},
                {0, 0, 0}
        };
        int result = service.checkWin(win);

        assertEquals(1, result);
    }

    @Test
    void shouldDetectWinInColumn() {
        int[][] win = {
                {1, 0, 0},
                {1, 0, 0},
                {1, 0, 0}
        };
        int result = service.checkWin(win);

        assertEquals(1, result);
    }

    @Test
    void shouldDetectWinInDiagonal() {
        int[][] win = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        int result = service.checkWin(win);

        assertEquals(1, result);
    }

    @Test
    void shouldDetectGameOver() {
        Game game = new Game(UUID.randomUUID(), new GameBoard());
        game.getGameBoard().setValue(0, 0, 1);
        game.getGameBoard().setValue(0, 1, 1);
        game.getGameBoard().setValue(0,2, 1);
        boolean isOver = service.isGameOver(game);

        assertTrue(isOver);
    }

    @Test
    void shouldUpdateGameStateToWin() {
        Game game = new Game(UUID.randomUUID(), new GameBoard());
        int[][] board = {
                {1, 0, 0},
                {1, 0, 0},
                {1, 0, 0}
        };
        game.setGameBoard(new GameBoard(board));
        service.updateGameState(game);

        assertEquals(GameStates.WIN, game.getState());
    }

    @Test
    void shouldValidateCorrectMove() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Game game = new Game(gameId, new GameBoard());
        game.setState(GameStates.IN_PROGRESS);
        game.setPlayerXId(playerId);
        game.setCurrentTurn(playerId);
        when(repository.existsById(gameId)).thenReturn(true);

        assertDoesNotThrow(() -> service.validateGame(game, playerId, 0, 0));
    }

    @Test
    void shouldThrowExceptionForInvalidPosition() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Game game = new Game(gameId, new GameBoard());
        game.setState(GameStates.IN_PROGRESS);
        game.setPlayerXId(playerId);
        game.setCurrentTurn(playerId);
        when(repository.existsById(gameId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.validateGame(game, playerId, 0, 4));
        assertThrows(IllegalArgumentException.class, () -> service.validateGame(game, playerId, 5, 2));
        assertThrows(IllegalArgumentException.class, () -> service.validateGame(game, playerId, -1, 4));
    }

    @Test
    void shouldThrowExceptionForWrongPlayerTurn() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        UUID playerOId = UUID.randomUUID();

        Game game = new Game(gameId, new GameBoard());
        game.setState(GameStates.IN_PROGRESS);
        game.setPlayerXId(playerId);
        game.setPlayerOId(playerOId);
        game.setCurrentTurn(playerId);
        when(repository.existsById(gameId)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.validateGame(game, playerOId, 0, 0));
    }

    @Test
    void shouldThrowExceptionWithWrongState() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Game game = new Game(gameId, new GameBoard());
        game.setState(GameStates.WIN);
        game.setPlayerXId(playerId);
        when(repository.existsById(gameId)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.validateGame(game, playerId, 0, 0));
    }

    @Test
    void shouldThrowExceptionOccupiedCell() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        Game game = new Game(gameId, new GameBoard());
        game.setState(GameStates.IN_PROGRESS);
        game.setPlayerXId(playerId);
        game.getGameBoard().setValue(0, 0, 1);
        when(repository.existsById(gameId)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.validateGame(game, playerId, 0, 0));
    }

    @Test
    void shouldThrowExceptionForWrongPlayerId() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        UUID playerOId = UUID.randomUUID();

        Game game = new Game(gameId, new GameBoard());
        game.setState(GameStates.IN_PROGRESS);
        game.setPlayerXId(playerId);
        game.setPlayerOId(playerOId);
        game.setCurrentTurn(playerId);
        when(repository.existsById(gameId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.validateGame(game, UUID.randomUUID(), 0, 0));
    }

    @Test
    void shouldMakeMoveAndSwitchTurn() {
        UUID gameId = UUID.randomUUID();
        UUID playerXId = UUID.randomUUID();
        UUID playerOId = UUID.randomUUID();

        Game game = new Game(gameId, new  GameBoard());
        game.setState(GameStates.IN_PROGRESS);
        game.setPlayerXId(playerXId);
        game.setPlayerOId(playerOId);
        game.setCurrentTurn(playerXId);
        when(repository.save(any(GameEntity.class))).thenReturn(null);

        service.makeMove(game, playerXId, 0, 0);
        assertEquals(1, game.getGameBoard().getValue(0, 0));
        assertEquals(playerOId, game.getCurrentTurn());
        verify(repository, times(1)).save(any(GameEntity.class));
    }
}
