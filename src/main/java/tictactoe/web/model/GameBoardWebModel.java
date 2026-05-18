package tictactoe.web.model;

public class GameBoardWebModel {
    private int[][] board;

    public GameBoardWebModel() { }

    public GameBoardWebModel(int[][] board) {
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }
}
