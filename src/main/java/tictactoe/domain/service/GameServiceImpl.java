package tictactoe.domain.service;

import tictactoe.datasource.mapper.GameEntityMapper;
import tictactoe.datasource.repository.GameRepository;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameBoard;
import tictactoe.datasource.model.GameEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import tictactoe.domain.model.Game.GameStates;
import tictactoe.web.mapper.GameWebMapper;
import tictactoe.web.model.GameWebModel;
import tictactoe.domain.model.Game.Token;

public class GameServiceImpl implements GameService{
    private final GameRepository repository;

    public GameServiceImpl(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public Game getGame(UUID id) {
        Optional<GameEntity> entityOpt = repository.findById(id);
        if (entityOpt.isEmpty()) {
            throw new IllegalStateException("Game with ID: " + id + ", doesn't exist");
        }
        GameEntity entity = entityOpt.get();
        Game game = GameEntityMapper.toDomain(entity);
        return game;
    }

    @Override
    public int[] getNextMove(Game game) {
        return findBestMove(game.getGameBoard().getBoard());
    }

    @Override
    public void validateGame(Game game, UUID userId, int row, int col) {
        if (game.getId() == null || !repository.existsById(game.getId())) {
            throw new IllegalStateException("Game with given id doesn't exist");
        }
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (!userId.equals(game.getPlayerXId()) && !userId.equals(game.getPlayerOId())) {
            throw new IllegalArgumentException("Wrong user id");
        }
        if (!userId.equals(game.getCurrentTurn())) {
            throw new IllegalStateException("Not your turn!");
        }
        if (!game.getGameBoard().isCellEmpty(row, col)) {
            throw new IllegalStateException("Cell is already occupied!");
        }
        if (game.getState() != GameStates.WAITING_FOR_PLAYERS && game.getState() != GameStates.IN_PROGRESS) {
            throw new IllegalStateException("Game is not active");
        }
    }

    @Override
    public void makeMove(Game game, UUID userId, int row, int col) {
        Token token = userId.equals(game.getPlayerXId()) ? Token.X : Token.O;
        game.getGameBoard().setValue(row, col, token.getValue());
        updateGameState(game);
        if (game.getState() == GameStates.IN_PROGRESS) {
            if (game.isVsComputer()) {
                int[] move = getNextMove(game);
                game.getGameBoard().setValue(move[0], move[1], Token.O.getValue());
                updateGameState(game);
            } else {
                game.setCurrentTurn(userId.equals(game.getPlayerXId())
                        ? game.getPlayerOId()
                        : game.getPlayerXId()
                );
            }
        }
        repository.save(GameEntityMapper.toEntity(game));
    }

    public List<GameWebModel> findAvailableGames() {
        Iterable<GameEntity> games = repository.findAll();
        List<GameWebModel> availableGames = new ArrayList<>();
        for (GameEntity game : games) {
            if (game.getState() != GameStates.WIN && game.getState() != GameStates.DRAW) {
                availableGames.add(GameWebMapper.toWeb(GameEntityMapper.toDomain(game)));
            }
        }
        return availableGames;
    }

    public List<UUID> findAvailableGamesIds() {
        Iterable<GameEntity> games = repository.findAll();
        List<UUID> IDs = new ArrayList<>();
        for (GameEntity game : games) {
            if (game.getState() != GameStates.WIN && game.getState() != GameStates.DRAW) {
                IDs.add(GameWebMapper.toWeb(GameEntityMapper.toDomain(game)).getId());
            }
        }
        return IDs;
    }

    @Override
    public void updateGameState(Game game) {
        if (game == null) {
            throw new IllegalStateException("No such game");
        }
        int winner = checkWin(game.getGameBoard().getBoard());
        if (winner != 0) {
            game.setState(GameStates.WIN);
            return;
        }
        if (isBoardFull(game.getGameBoard().getBoard())) {
            game.setState(GameStates.DRAW);
            return;
        }
        game.setState(GameStates.IN_PROGRESS);
    }

    @Override
    public boolean isGameOver(Game game) {
        if (game == null) {
            throw new IllegalStateException("No such game");
        }
        return checkWin(game.getGameBoard().getBoard()) != 0 || isBoardFull(game.getGameBoard().getBoard());
    }

    @Override
    public Game createGame(String mode, UUID userId) {
        Game game = new Game(UUID.randomUUID(), new GameBoard());
        if (mode.equals("PC")) {
            game.setVsComputer(true);
            game.setState(GameStates.IN_PROGRESS);
        } else {
            game.setState(GameStates.WAITING_FOR_PLAYERS);
            game.setVsComputer(false);
        }
        game.setPlayerXId(userId);
        game.setCurrentTurn(userId);
        repository.save(GameEntityMapper.toEntity(game));
        return game;
    }

    @Override
    public void saveGame(Game game) {
        repository.save(GameEntityMapper.toEntity(game));
    }

    private int[] findBestMove(int[][] board) {
        int bestScore = Integer.MAX_VALUE;
        int[] move = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = -1;
                    int score = minimax(board, true);
                    board[i][j] = 0;
                    if (score < bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    private int minimax(int[][] board, boolean isMaxi) {
        int win = checkWin(board);
        if (win != 0) return win;
        if (isBoardFull(board)) return 0;
        if (isMaxi) {
            int maxScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = 1;
                        int score = minimax(board,false);
                        board[i][j] = 0;
                        maxScore = Math.max(score, maxScore);
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = -1;
                        int score = minimax(board,true);
                        board[i][j] = 0;
                        minScore = Math.min(score, minScore);
                    }
                }
            }
            return minScore;
        }
    }

    @Override
    public int checkWin(int[][] board) {
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0];   //diagonals
        if (board[0][0] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2];
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0];   //rows
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i];   //cols
        }
        return 0;
    }

    private boolean isBoardFull(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) return false;
            }
        }
        return true;
    }
}