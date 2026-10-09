package mx.edu.fesaragon.login.repository;

import mx.edu.fesaragon.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(
            String username,
            String email
    );
}
