package ray1024.imageservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    private JwtParser parser() {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build();
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