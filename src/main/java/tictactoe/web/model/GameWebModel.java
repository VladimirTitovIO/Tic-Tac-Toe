package tictactoe.web.model;

import java.util.UUID;
import tictactoe.domain.model.Game.GameStates;


public class GameWebModel {
    private UUID id;
    private GameBoardWebModel gameBoard;
    private UUID playerX;
    private UUID playerO;
    private UUID currentTurn;
    private GameStates state;
    private boolean vsComputer;

    public GameWebModel(UUID id, GameBoardWebModel gameBoard, boolean vsComputer) {
        this.id = id;
        this.gameBoard = gameBoard;
        this.vsComputer = vsComputer;
    }

    public boolean isVsComputer() { return vsComputer; }
    public void setVsComputer(boolean vsComputer) { this.vsComputer = vsComputer; }
    public UUID getId() { return id; }
    public GameBoardWebModel getGameBoard() {
        return gameBoard;
    }
    public UUID getPlayerXId() { return playerX; }
    public UUID getPlayerOId() { return playerO; }
    public UUID getCurrentTurn() { return currentTurn; }
    public GameStates getState() { return state; }
    public void setPlayerXId(UUID id) { playerX = id; }
    public void setPlayerOId(UUID id) { playerO = id; }
    public void setCurrentTurn(UUID id) { currentTurn = id; }
    public void setState(GameStates state) { this.state = state; }
}
