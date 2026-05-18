package tictactoe.domain.model;

import java.util.UUID;

public class Game {
    private final UUID id;
    private GameBoard gameBoard;

    private UUID playerX;
    private UUID playerO;
    private UUID currentTurn;
    private GameStates state;
    private boolean vsComputer;

    public Game(UUID id, GameBoard gameBoard) {
        this.id = id;
        this.gameBoard = gameBoard;
    }

    public UUID getPlayerXId() { return playerX; }
    public UUID getPlayerOId() { return playerO; }
    public UUID getCurrentTurn() { return currentTurn; }
    public GameStates getState() { return state; }
    public boolean isVsComputer() { return vsComputer; }
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    public UUID getId() {
        return id;
    }
    public void setVsComputer(boolean vsComputer) { this.vsComputer = vsComputer; }
    public void setCurrentTurn(UUID playerUUID) { currentTurn = playerUUID; }
    public void setState(GameStates state) { this.state = state; }
    public void setPlayerXId(UUID id) { this.playerX = id; }
    public void setPlayerOId(UUID id) { this.playerO = id; }
    public void setGameBoard(GameBoard gameBoard) { this.gameBoard = gameBoard; }


    public enum Token {
        X(1),
        O(-1);

        private final int value;

        Token(int value) {
            this.value = value;
        }
        public int getValue() { return value; }

    }

    public enum GameStates {
        WAITING_FOR_PLAYERS,
        IN_PROGRESS,
        DRAW,
        WIN
    }

}
