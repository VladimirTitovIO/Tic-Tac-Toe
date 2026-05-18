package tictactoe.di;

import org.springframework.context.annotation.Configuration;
import tictactoe.datasource.repository.GameRepository;
import tictactoe.datasource.repository.UserRepository;
import tictactoe.domain.service.GameService;
import tictactoe.domain.service.GameServiceImpl;
import org.springframework.context.annotation.Bean;
import tictactoe.domain.service.UserService;
import tictactoe.domain.service.UserServiceImpl;
import tictactoe.web.model.AuthFilter;
import tictactoe.web.model.AuthorizationService;
import tictactoe.web.model.AuthorizationServiceImpl;

@Configuration
public class AppConfiguration {

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public AuthorizationService authorizationService(UserService userService) {
        return new AuthorizationServiceImpl(userService);
    }

    @Bean
    public AuthFilter authFilter(AuthorizationService authService) {
        return new AuthFilter(authService);
    }

    @Bean
    public GameService gameService(GameRepository repository) {
        return new GameServiceImpl(repository);
    }
}
