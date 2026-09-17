package tictactoe.datasource.model;

import jakarta.persistence.*;
import tictactoe.domain.model.Game.GameStates;

import java.util.UUID;

@Entity
@Table(name = "games")
public class GameEntity {
    @Id
    private UUID id;

    @Column
    private String board;

    @Column
    private UUID playerXId;

    @Column
    private UUID playerOId;

    @Column
    private UUID currentTurn;

    @Column
    private GameStates state;

    @Column
    private boolean vsComputer;

    public boolean isVsComputer() { return vsComputer; }
    public void setVsComputer(boolean vsComputer) { this.vsComputer = vsComputer;}
    public void setId(UUID id) { this.id = id; }
    public void setBoard(String board) { this.board = board; }
    public void setPlayerXId(UUID id) { playerXId = id; }
    public void setPlayerOId(UUID id) { playerOId = id; }
    public void setCurrentTurn(UUID id) { currentTurn = id; }
    public void setGameState(GameStates state) { this.state = state; }
    public String getBoard() { return board; }
    public UUID getId() { return id; }
    public UUID getPlayerXId() { return playerXId; }
    public UUID getPlayerOId() { return playerOId; }
    public UUID getCurrentTurn() { return currentTurn; }
    public GameStates getState() { return state; }

    public GameEntity() {}

    public String convertBoardToString(int[][] board) {
        String result = " ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j]);
                if (j < board[i].length - 1) sb.append(",");
            }
            if (i < board.length - 1) sb.append(";");
        }
        result = sb.toString();
        return result;
    }

    public int[][] convertStringToBoard(String board) {
        int[][] gameBoard = new int[3][3];
        String rows[] = board.split(";");
        for (int i = 0; i < 3; i++) {
            String[] values = rows[i].split(",");
            for (int j = 0; j < 3; j++) {
                gameBoard[i][j] = Integer.parseInt(values[j]);
            }
        }
        return gameBoard;
    }
}
