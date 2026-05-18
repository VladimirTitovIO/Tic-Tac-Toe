package tictactoe.datasource.model;

public class GameBoardData {
    private final int[][] gameBoard;

    public GameBoardData(int[][] gameBoard) {
        if (gameBoard.length != 3 || gameBoard[0].length != 3) throw new IllegalStateException("Invalid board size. expected 3x3, got: " + gameBoard.length + "x" + gameBoard[0].length);
        this.gameBoard = gameBoard;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }
}
