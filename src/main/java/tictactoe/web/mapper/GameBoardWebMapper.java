package tictactoe.web.mapper;

import tictactoe.domain.model.GameBoard;
import tictactoe.web.model.GameBoardWebModel;

public class GameBoardWebMapper {
    public static GameBoard toDomain(GameBoardWebModel web) {
        return new GameBoard(web.getBoard());
    }

    public static GameBoardWebModel toWeb(GameBoard domain) {
        return new GameBoardWebModel(domain.getBoard());
    }
}
