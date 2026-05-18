package tictactoe.datasource.mapper;

import tictactoe.datasource.model.GameBoardData;
import tictactoe.domain.model.GameBoard;

public class GameBoardMapper {
    public static GameBoardData toData(GameBoard domain) { return new GameBoardData(domain.getBoard());
    }

    public static GameBoard toGameBoard(GameBoardData datasource) {
        return new GameBoard(datasource.getGameBoard());
    }
}
