package tictactoe.datasource.mapper;

import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameBoard;
import tictactoe.datasource.model.GameEntity;

public class GameEntityMapper {
    public static GameEntity toEntity(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        if (game.getId() == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }
        if (game.getGameBoard() == null) {
            throw new IllegalArgumentException("GameBoard cannot be null");
        }

        GameEntity entity = new GameEntity();
        entity.setId(game.getId());
        entity.setBoard(entity.convertBoardToString(game.getGameBoard().getBoard()));
        entity.setVsComputer(game.isVsComputer());
        entity.setGameState(game.getState());
        entity.setCurrentTurn(game.getCurrentTurn());
        entity.setPlayerXId(game.getPlayerXId());
        entity.setPlayerOId(game.getPlayerOId());
        return entity;
    }

    public static Game toDomain(GameEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }
        if (entity.getBoard().isEmpty()) {
            throw new IllegalArgumentException("GameBoard cannot be null");
        }

        Game game = new Game(entity.getId(), new GameBoard(entity.convertStringToBoard(entity.getBoard())));
        game.setVsComputer(entity.isVsComputer());
        game.setCurrentTurn(entity.getCurrentTurn());
        game.setPlayerXId(entity.getPlayerXId());
        game.setPlayerOId(entity.getPlayerOId());
        game.setState(entity.getState());
        return game;
    }

}
