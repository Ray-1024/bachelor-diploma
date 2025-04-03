package ray1024.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.userservice.model.entity.RefreshJwtToken;
import ray1024.userservice.model.entity.UserEntity;

import java.util.Optional;

public interface RefreshJwtTokenRepository extends JpaRepository<RefreshJwtToken, Long> {
    Optional<RefreshJwtToken> findByUser(UserEntity user);

    Optional<RefreshJwtToken> findByRefreshToken(String refreshToken);
}
