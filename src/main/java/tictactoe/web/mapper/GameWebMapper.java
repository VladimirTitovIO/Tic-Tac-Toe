package tictactoe.web.mapper;

import tictactoe.domain.model.Game;
import tictactoe.web.model.GameWebModel;

public class GameWebMapper {
    public static Game toDomain(GameWebModel web) {
        if (web == null) {
            throw new IllegalArgumentException("GameWeb cannot be null");
        }
        if (web.getId() == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }
        if (web.getGameBoard() == null) {
            throw new IllegalArgumentException("GameBoard cannot be null");
        }
        Game game = new Game(web.getId(), GameBoardWebMapper.toDomain(web.getGameBoard()));
        game.setVsComputer(web.isVsComputer());
        game.setCurrentTurn(web.getCurrentTurn());
        game.setPlayerOId(web.getPlayerOId());
        game.setPlayerXId(web.getPlayerXId());
        game.setState(web.getState());
        return game;
    }

    public static GameWebModel toWeb(Game domain) {
        if (domain == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        if (domain.getId() == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }
        if (domain.getGameBoard() == null) {
            throw new IllegalArgumentException("GameBoard cannot be null");
        }
        GameWebModel model = new GameWebModel(domain.getId(), GameBoardWebMapper.toWeb(domain.getGameBoard()), domain.isVsComputer());
        model.setCurrentTurn(domain.getCurrentTurn());
        model.setPlayerOId(domain.getPlayerOId());
        model.setPlayerXId(domain.getPlayerXId());
        model.setVsComputer(domain.isVsComputer());
        model.setState(domain.getState());
        return model;
    }
}
