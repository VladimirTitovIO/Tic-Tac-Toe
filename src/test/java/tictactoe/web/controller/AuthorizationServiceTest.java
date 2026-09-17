//package tictactoe.web.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import tictactoe.domain.model.User;
//import tictactoe.domain.model.User.Role;
//import tictactoe.domain.service.UserService;
//import tictactoe.web.model.DefaultAuthorizationService;
//
//import java.util.Base64;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class AuthorizationServiceTest {
//
//    @Mock
//    private UserService userService;
//
//    private DefaultAuthorizationService authService;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//        authService = new DefaultAuthorizationService(userService);
//    }
//
//    @Test
//    void shouldAuthorizeValidUser() {
//        UUID id = UUID.randomUUID();
//        User user = new User(id, "testuser", "password123", List.of(Role.USER));
//        when(userService.findByLogin("testuser")).thenReturn(user);
//        String credentials = Base64.getEncoder().encodeToString("testuser:password123".getBytes());
//        UUID result = authService.authorize(credentials);
//
//        assertEquals(id, result);
//    }
//
//    @Test
//    void shouldThrowForUnauthorizedAccess() {
//        UUID id = UUID.randomUUID();
//        User user = new User(id, "testuser", "password123", List.of(Role.USER));
//        when(userService.findByLogin("testuser")).thenReturn(user);
//        String credentials = Base64.getEncoder().encodeToString("testuser:password321".getBytes());
//        assertThrows(IllegalStateException.class, () -> authService.authorize(credentials));
//    }
//}
