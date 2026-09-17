//package tictactoe.web.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import tictactoe.datasource.repository.UserRepository;
//import tictactoe.domain.model.Game;
//import tictactoe.domain.model.GameBoard;
//import tictactoe.domain.service.GameService;
//import tictactoe.web.model.AuthorizationService;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.junit.jupiter.api.Assertions.*;
//import tictactoe.domain.model.Game.GameStates;
//
//@WebMvcTest(Controller.class)
//@AutoConfigureMockMvc(addFilters = false)
//public class ControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private GameService gameService;
//
//    @MockBean
//    private AuthorizationService authService;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void shouldCreateGameVsPc() throws Exception {
//        Controller controller = new Controller(gameService, authService, null);
//        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();
//        UUID userId = UUID.randomUUID();
//        Game game = new Game(UUID.randomUUID(), new GameBoard());
//        ObjectMapper mapper = new ObjectMapper();
//
//        when(gameService.createGame(anyString(), Mockito.eq(userId))).thenReturn(game);
//
//        mvc.perform(post("/game")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(
//                                Map.of(
//                                        "mode", "PC",
//                                        "userId", userId.toString()
//                                )
//                        )))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void shouldCreateGameVsPlayer() throws Exception {
//        Controller controller = new Controller(gameService, authService, null);
//        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();
//        UUID userId = UUID.randomUUID();
//        Game game = new Game(UUID.randomUUID(), new GameBoard());
//        ObjectMapper mapper = new ObjectMapper();
//
//        when(gameService.createGame(anyString(), Mockito.eq(userId))).thenReturn(game);
//
//        mvc.perform(post("/game")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(
//                                Map.of(
//                                        "mode", "Player",
//                                        "userId", userId.toString()
//                                )
//                        )))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void shouldReturnBadRequestForInvalidMode() throws Exception {
//        Controller controller = new Controller(gameService, authService, null);
//        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();
//        UUID userId = UUID.randomUUID();
//        Game game = new Game(UUID.randomUUID(), new GameBoard());
//        ObjectMapper mapper = new ObjectMapper();
//
//        when(gameService.createGame(anyString(), Mockito.eq(userId))).thenReturn(game);
//
//        mvc.perform(post("/game")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(
//                        Map.of("mod", "mode",
//                                "userId", userId.toString())
//                    )))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldGetGame() throws Exception {
//        UUID gameId = UUID.randomUUID();
//        Game game = new Game(gameId, new GameBoard());
//
//        when(gameService.getGame(gameId)).thenReturn(game);
//
//        mockMvc.perform(get("/game/" + gameId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(gameId.toString()));
//    }
//
//    @Test
//    void shouldJoinGame() throws Exception {
//        UUID id = UUID.randomUUID();
//        UUID playerXId = UUID.randomUUID();
//        UUID playerOId = UUID.randomUUID();
//        Game game = new Game(id, new GameBoard());
//        game.setVsComputer(false);
//        game.setPlayerXId(playerXId);
//        game.setPlayerOId(null);
//        game.setState(GameStates.WAITING_FOR_PLAYERS);
//
//        when(authService.authorize(anyString())).thenReturn(playerOId);
//        when(gameService.getGame(id)).thenReturn(game);
//
//        mockMvc.perform(post("/game/join/" + id)
//                        .header("Authorization", "Basic dXNlcjI6NDMyMQ")
//                        .with(csrf()))
//                .andExpect(status().isOk());
//        assertEquals(playerOId, game.getPlayerOId());
//
//        verify(gameService, times(1)).saveGame(any(Game.class));
//    }
//
//    @Test
//    void shouldNotJoinPcGame() throws Exception {
//        UUID gameId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        Game game = new Game(gameId, new GameBoard());
//        game.setVsComputer(true);
//
//        when(authService.authorize(anyString())).thenReturn(userId);
//        when(gameService.getGame(gameId)).thenReturn(game);
//
//        mockMvc.perform(post("/game/join/" + gameId))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void shouldMakeMove() throws Exception {
//        UUID gameId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        Game game = new Game(gameId, new GameBoard());
//        game.setPlayerXId(userId);
//        game.setCurrentTurn(userId);
//        game.setState(GameStates.IN_PROGRESS);
//
//        when(authService.authorize(anyString())).thenReturn(userId);
//        when(gameService.getGame(gameId)).thenReturn(game);
//        when(gameService.isGameOver(any())).thenReturn(false);
//
//        Map<String, String> move = new HashMap<>();
//        move.put("row", "0");
//        move.put("column", "0");
//
//        mockMvc.perform(post("/game/" + gameId)
//                .header("Authorization", "Basic dXNlcjoxMjM0")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(move)))
//                .andExpect(status().isOk());
//
//        verify(gameService, times(1)).makeMove(any(), any(), eq(0), eq(0));
//    }
//
//    @Test
//    void shouldReturnUserById() throws Exception {
//        UUID gameId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        Game game = new Game(gameId, new GameBoard());
//        game.setPlayerXId(userId);
//
//        when(gameService.getGame(gameId)).thenReturn(game);
//
//        mockMvc.perform(get("/game/users/" + userId)
//                .header("Authorization", "Basic dXNlcjoxMjM0"))
//                .andExpect(status().isOk());
//    }
//}