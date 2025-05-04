package ray1024.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.userservice.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
