package tictactoe.di;

import org.springframework.context.annotation.Configuration;
import tictactoe.datasource.repository.GameRepository;
import tictactoe.datasource.repository.UserRepository;
import tictactoe.domain.service.GameService;
import tictactoe.domain.service.GameServiceImpl;
import org.springframework.context.annotation.Bean;
import tictactoe.domain.service.UserService;
import tictactoe.domain.service.UserServiceImpl;
import tictactoe.web.model.*;

@Configuration
public class AppConfiguration {
    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public AuthorizationService authorizationService(UserService userService, JwtProvider jwtProvider, JwtUtil jwtUtil) {
        return new DefaultAuthorizationService(userService, jwtProvider, jwtUtil);
    }

    @Bean
    public AuthFilter authFilter(AuthorizationService authService, JwtProvider jwtProvider, UserService userService, JwtUtil jwtUtil) {
        return new AuthFilter(authService, jwtProvider, userService, jwtUtil);
    }

    @Bean
    public GameService gameService(GameRepository repository) {
        return new GameServiceImpl(repository);
    }
}
