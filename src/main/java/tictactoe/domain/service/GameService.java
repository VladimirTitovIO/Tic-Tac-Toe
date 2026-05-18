package tictactoe.domain.service;
import tictactoe.domain.model.Game;
import tictactoe.web.model.GameWebModel;

import java.util.List;
import java.util.UUID;

public interface GameService {
    int[] getNextMove(Game game);
    void updateGameState(Game game);
    boolean isGameOver(Game game);
    Game createGame(String mode, UUID userId);
    Game getGame(UUID id);
    void saveGame(Game game);
    int checkWin(int[][] board);
    List<GameWebModel> findAvailableGames();
    List<UUID> findAvailableGamesIds();
    void validateGame(Game game, UUID userId, int row, int col);
    void makeMove(Game game, UUID userId, int row, int col);
}
