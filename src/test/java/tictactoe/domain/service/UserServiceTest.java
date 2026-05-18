package tictactoe.domain.service;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tictactoe.datasource.model.UserEntity;
import tictactoe.datasource.repository.UserRepository;
import tictactoe.domain.model.User;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository);
    }

    @Test
    void shouldCreateUser() {
        when(repository.save(any(UserEntity.class))).thenReturn(null);
        User user = service.createUser("testuser", "password123");

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("testuser", user.getLogin());
        assertEquals("password123", user.getPassword());
        verify(repository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void shouldFindUserByLogin() {
        UUID userId = UUID.randomUUID();
        UserEntity entity = new UserEntity(userId, "testuser", "password123");
        when(repository.findByLogin("testuser")).thenReturn(Optional.of(entity));
        User user = service.findByLogin("testuser");

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(userId, user.getId());
        assertEquals("testuser", user.getLogin());
        verify(repository, times(1)).findByLogin("testuser");
    }

    @Test
    void shouldReturnNullWhenUserNotFoundByLogin() {
        when(repository.findByLogin("nottestuser")).thenReturn(Optional.empty());
        User user = service.findByLogin("nottestuser");
        assertNull(user);
    }

    @Test
    void shouldFindUserById() {
        UUID userId = UUID.randomUUID();
        UserEntity entity = new UserEntity(userId, "testuser", "password123");
        when(repository.findById(userId)).thenReturn(Optional.of(entity));
        User user = service.findById(userId);

        assertNotNull(user);
        assertEquals(userId, user.getId());
        verify(repository, times(1)).findById(userId);
    }
}
