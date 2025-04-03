package ray1024.userservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ray1024.userservice.exception.TokenNotFoundException;
import ray1024.userservice.exception.UserNotFoundException;
import ray1024.userservice.model.dto.TokensDto;
import ray1024.userservice.model.entity.RefreshJwtToken;
import ray1024.userservice.model.entity.UserEntity;
import ray1024.userservice.repository.RefreshJwtTokenRepository;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserService userService;

    private final RefreshJwtTokenRepository refreshJwtTokenRepository;

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    @Value("${jwt.token.expire-time}")
    private long jwtExpiration;

    @Value("${jwt.token.refresh-time}")
    private long refreshTokenDuration;


    private String jwtTokenByUsername(@NonNull String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(jwtExpiration)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String extractUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isJwtTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private RefreshJwtToken refreshJwtTokenByUsername(String username) {
        return refreshJwtTokenRepository.save(
                RefreshJwtToken.builder()
                        .user(userService.findByUsernameO(username).orElseThrow(() -> new UserNotFoundException(username)))
                        .expiryInstant(Instant.now().plusMillis(refreshTokenDuration))
                        .refreshToken(UUID.randomUUID().toString())
                        .build()
        );
    }

    private TokensDto updateRefreshToken(UserEntity user, RefreshJwtToken token) {
        if (token.getExpiryInstant().compareTo(Instant.now()) < 0) {
            refreshJwtTokenRepository.delete(token);
            token = refreshJwtTokenByUsername(user.getUsername());
        }
        return TokensDto.builder()
                .token(jwtTokenByUsername(user.getUsername()))
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public TokensDto updateRefreshTokenByUsername(@NonNull String username) {
        UserEntity user = userService.findByUsername(username);
        RefreshJwtToken token = refreshJwtTokenRepository.findByUser(user).orElseThrow();
        return updateRefreshToken(user, token);
    }


    public TokensDto updateRefreshTokenByToken(@NonNull String refreshToken) {
        RefreshJwtToken token = refreshJwtTokenRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new TokenNotFoundException(refreshToken)
        );
        UserEntity user = token.getUser();
        return updateRefreshToken(user, token);
    }
}