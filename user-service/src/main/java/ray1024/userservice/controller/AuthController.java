package ray1024.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ray1024.userservice.model.entity.User;
import ray1024.userservice.model.request.UsernamePasswordRequest;
import ray1024.userservice.model.response.TokenResponse;
import ray1024.userservice.service.JwtService;
import ray1024.userservice.service.UserService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping
    public TokenResponse signUp(@RequestBody UsernamePasswordRequest usernamePasswordRequest) {
        User user = userService.create(usernamePasswordRequest.getUsername(), usernamePasswordRequest.getPassword());
        String token = jwtService.getToken(user);
        return TokenResponse.builder()
                .token(token)
                .build();
    }

    @PutMapping
    public TokenResponse signIn(@RequestBody UsernamePasswordRequest usernamePasswordRequest) {
        User user = userService.findByUsernameAndPassword(usernamePasswordRequest.getUsername(), usernamePasswordRequest.getPassword());
        String token = jwtService.getToken(user);
        return TokenResponse.builder()
                .token(token)
                .build();
    }
}
