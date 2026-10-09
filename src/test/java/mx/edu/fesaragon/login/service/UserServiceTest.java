package mx.edu.fesaragon.login.service;

import mx.edu.fesaragon.login.dto.RegisterRequest;
import mx.edu.fesaragon.login.model.User;
import mx.edu.fesaragon.login.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new Argon2PasswordEncoder(
                16,
                32,
                1,
                19456,
                2
        );

        userService = new UserService(
                userRepository,
                passwordEncoder
        );
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest(
                "usuario_test",
                "usuario@example.com",
                "ClaveSegura123!"
        );

        userService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldStorePasswordAsHash() {
        String rawPassword = "ClaveSegura123!";

        RegisterRequest request = new RegisterRequest(
                "usuario_hash",
                "hash@example.com",
                rawPassword
        );

        userService.register(request);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertNotEquals(
                rawPassword,
                savedUser.getPassword()
        );

        assertTrue(
                passwordEncoder.matches(
                        rawPassword,
                        savedUser.getPassword()
                )
        );
    }

    @Test
    void shouldRejectDuplicateUsername() {
        RegisterRequest request = new RegisterRequest(
                "usuario_existente",
                "nuevo@example.com",
                "ClaveSegura123!"
        );

        when(userRepository.existsByUsername("usuario_existente"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        RegisterRequest request = new RegisterRequest(
                "usuario_nuevo",
                "existente@example.com",
                "ClaveSegura123!"
        );

        when(userRepository.existsByEmail("existente@example.com"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldRejectBlankUsername() {
        RegisterRequest request = new RegisterRequest(
                "   ",
                "usuario@example.com",
                "ClaveSegura123!"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldRejectBlankEmail() {
        RegisterRequest request = new RegisterRequest(
                "usuario_test",
                "   ",
                "ClaveSegura123!"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldRejectBlankPassword() {
        RegisterRequest request = new RegisterRequest(
                "usuario_test",
                "usuario@example.com",
                "   "
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }
}


