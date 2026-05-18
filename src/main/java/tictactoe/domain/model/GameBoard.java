package tictactoe.domain.model;

public class GameBoard {
    private final int[][] board;

    public GameBoard() {
        this.board = new int[3][3];
    }

    public GameBoard(int[][] gameBoard) {
        if (gameBoard.length != 3 || gameBoard[0].length != 3) throw new IllegalStateException("Invalid board size. expected 3x3, got: " + gameBoard.length + "x" + gameBoard[0].length);
        this.board = gameBoard;
    }

    public void setValue(int row, int col, int value) {
        if (row >= 3 || col >= 3 || row < 0 || col < 0) throw new IllegalStateException("Wrong row/column size. Min size = 0, max size = 2");
        if (!isCellEmpty(row, col)) throw new IllegalStateException("Cell is already occupied");
        board[row][col] = value;
    }

    public int getValue(int row, int col) {
        if (row >= 3 || col >= 3 || row < 0 || col < 0) throw new IllegalStateException("Wrong row/column size. Min size = 0, max size = 2");
        return board[row][col];
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isCellEmpty(int row, int col) {
        if (row >= 3 || col >= 3 || row < 0 || col < 0) throw new IllegalStateException("Wrong row/column size. Min size = 0, max size = 2");
        return board[row][col] == 0;
    }

}