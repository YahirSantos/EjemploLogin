package mx.edu.fesaragon.login.service;

import mx.edu.fesaragon.login.config.PasswordConfig;
import mx.edu.fesaragon.login.dto.RegisterRequest;
import mx.edu.fesaragon.login.model.User;
import mx.edu.fesaragon.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register (RegisterRequest request){
        if(request == null){
            throw new IllegalArgumentException(
                    "La solicitud de registro es obligatoria.");
        }
        if(request.getUsername() == null || request.getUsername().isBlank()){
            throw new IllegalArgumentException(
                    "El nombre de usuario es obligatorio.");

        }
        if(request.getEmail() == null || request.getEmail().isBlank()){
            throw new IllegalArgumentException(
                    "El email es obligatorio.");

        }
        if(request.getPassword() == null || request.getPassword().isBlank()){
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria.");

        }
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException(
                    "El nombre de usuario ya está registrado");
        }
        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException(
                    "El email ya está registrado");
        }
        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = new User(
                username,
                email,
                passwordHash
        );

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean authenticate(String usernameOrEmail, String password) {

        if (usernameOrEmail == null || usernameOrEmail.isBlank()
                || password == null || password.isBlank()) {
            return false;
        }

        String identifier = usernameOrEmail.trim();

        Optional<User> userResult =
                userRepository.findByUsernameOrEmail(
                        identifier,
                        identifier.toLowerCase(Locale.ROOT)
                );

        if (userResult.isEmpty()) {
            return false;
        }

        User user = userResult.get();

        return passwordEncoder.matches(
                password,
                user.getPassword()
        );
    }
}
