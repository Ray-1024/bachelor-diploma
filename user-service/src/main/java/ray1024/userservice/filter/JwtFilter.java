package ray1024.userservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ray1024.userservice.model.entity.UserEntity;
import ray1024.userservice.service.JwtService;
import ray1024.userservice.service.UserService;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String JWT_AUTHORIZATION_PREFIX = "Bearer ";

    private JwtService jwtService;
    private UserService userService;

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
            if (token.isPresent() && jwtService.isJwtTokenValid(token.get())) {
                String username = jwtService.extractUsernameFromJwtToken(token.get());

                UserEntity user = userService.findByUsernameO(username).orElseThrow(() -> new UsernameNotFoundException(username));
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }
}
