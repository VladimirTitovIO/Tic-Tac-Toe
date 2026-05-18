package tictactoe.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tictactoe.datasource.model.UserEntity;
import tictactoe.datasource.repository.UserRepository;
import tictactoe.domain.model.Game;
import tictactoe.domain.model.GameBoard;
import tictactoe.domain.model.User;
import tictactoe.domain.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tictactoe.web.mapper.GameWebMapper;
import tictactoe.web.model.AuthorizationService;
import tictactoe.web.model.GameWebModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import tictactoe.domain.model.Game.GameStates;

@RestController
@RequestMapping("/game")
public class Controller {
    private final GameService gameService;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;

    public Controller(GameService gameService, AuthorizationService authorizationService, UserRepository userRepository) {
        this.gameService = gameService;
        this.authorizationService = authorizationService;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String test() {
        return "Game API is running. Insert id after '/game/'";
    }

    @PostMapping
    public ResponseEntity<GameWebModel> createGame(@RequestBody Map<String, String> request) {
        String mode = request.get("mode");
        UUID userId = UUID.fromString(request.get("userId"));
        if (mode == null || (!mode.equals("PC") && !mode.equals("Player"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid mode, choose PC or Player as your opponent");
        }
        Game game = gameService.createGame(mode, userId);
        if (mode.equals("Player")) game.setState(GameStates.WAITING_FOR_PLAYERS);
        GameWebModel gameWeb = GameWebMapper.toWeb(game);
        gameService.saveGame(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(gameWeb);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable("id") UUID id) {
        Game game = gameService.getGame(id);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(game);
    }

    @GetMapping("/users/{id}")
    public Optional<UserEntity> getUser(@PathVariable UUID id) {
        return userRepository.findById(id);
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<?> joinGame(@PathVariable("id") UUID id, @RequestHeader("Authorization") String header) {
        UUID userId = authorizationService.authorize(header.replace("Basic ", ""));
        Game game = gameService.getGame(id);
        if (game.isVsComputer()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot join a game versus PC");
        }
        if (game.getPlayerOId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both players have already joined the game");
        }
        if (game.getPlayerXId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot join your own game");
        }
        game.setPlayerOId(userId);
        game.setCurrentTurn(game.getPlayerXId());
        game.setState(GameStates.IN_PROGRESS);

        gameService.saveGame(game);

        return ResponseEntity.ok(GameWebMapper.toWeb(game));
    }

    @GetMapping("/available")
    public List<GameWebModel> getAvailableGames() {
        return gameService.findAvailableGames();
    }

    @GetMapping("/availableId")
    public List<UUID> getAvailableGamesIds() {
        return gameService.findAvailableGamesIds();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> makeMove(@PathVariable UUID id, @RequestBody Map<String, String> values, @RequestHeader("Authorization") String header) {
        UUID userId = authorizationService.authorize(header.replace("Basic ", ""));
        Game game = gameService.getGame(id);
        int row = Integer.parseInt(values.get("row"));
        int col = Integer.parseInt(values.get("column"));
        gameService.validateGame(game, userId, row, col);
        if (gameService.isGameOver(game)) {
            String winner = gameService.checkWin(game.getGameBoard().getBoard()) == -1
                    ? "PC"
                    : "Player";
            return ResponseEntity.ok(Map.of(
               "game", GameWebMapper.toWeb(game),
               "gameOver", true,
               "winner", winner
            ));
        }
        gameService.makeMove(game, userId, row, col);
        return ResponseEntity.ok(GameWebMapper.toWeb(game));
    }
}
