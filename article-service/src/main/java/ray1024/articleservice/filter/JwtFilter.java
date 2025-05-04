package ray1024.articleservice.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ray1024.articleservice.service.JwtService;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String JWT_AUTHORIZATION_PREFIX = "Bearer ";

    private JwtService jwtService;

    private Optional<String> parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(JWT_AUTHORIZATION_PREFIX)) {
            return Optional.of(headerAuth.substring(JWT_AUTHORIZATION_PREFIX.length()));
        }

        return Optional.empty();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> token = parseJwt(request);
            token.ifPresent(s -> {
                if (jwtService.isValid(s)) {
                    Claims claims = jwtService.getClaims(s);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(claims, s);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            });
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }
}
