package ray1024.userservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ray1024.userservice.model.entity.User;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    @Value("${jwt.token.expire-time}")
    private long jwtExpiration;

    public String getToken(@NonNull User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(
                        Jwts.claims(
                                new HashMap<>() {{
                                    put("id", user.getId());
                                    put("username", user.getUsername());
                                    put("authorities", user.getAuthorities());
                                }}
                        )
                )
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(jwtExpiration)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    private JwtParser parser() {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build();
    }

    public Instant getExpiration(@NonNull String token) {
        return getClaims(token).getExpiration().toInstant();
    }

    public Claims getClaims(@NonNull String token) {
        return parser()
                .parseClaimsJwt(token)
                .getBody();
    }

    public boolean isValid(@NonNull String token) {
        try {
            parser().parse(token);
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }
}