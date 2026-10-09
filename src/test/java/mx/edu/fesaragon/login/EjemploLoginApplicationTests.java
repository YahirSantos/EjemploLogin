package mx.edu.fesaragon.login;

import mx.edu.fesaragon.login.model.User;
import mx.edu.fesaragon.login.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class EjemploLoginApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUser() {
        User user = new User(
                "usuario_test",
                "usuario_test@example.com",
                "hash_psswrd"
        );
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());

        Optional<User> result =
                userRepository.findById(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals("usuario_test", result.get().getUsername());
        assertEquals(
                "usuario_test@example.com",
                result.get().getEmail()
        );
    }

}
