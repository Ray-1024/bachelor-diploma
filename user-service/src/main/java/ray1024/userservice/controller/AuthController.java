package ray1024.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ray1024.userservice.exception.UserNotFoundException;
import ray1024.userservice.model.dto.RefreshTokenDto;
import ray1024.userservice.model.dto.TokensDto;
import ray1024.userservice.model.dto.UsernamePasswordDto;
import ray1024.userservice.model.entity.UserEntity;
import ray1024.userservice.service.JwtService;
import ray1024.userservice.service.UserService;

@RestController(value = "/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;
    private JwtService jwtService;

    @PostMapping
    public TokensDto signUp(@RequestBody UsernamePasswordDto usernamePasswordDto) {
        UserEntity user = userService.create(usernamePasswordDto.getUsername(), usernamePasswordDto.getPassword());
        return jwtService.updateRefreshTokenByUsername(user.getUsername());
    }

    @PutMapping
    public TokensDto signIn(@RequestBody UsernamePasswordDto usernamePasswordDto) {
        UserEntity user = userService.findByUsernameAndPasswordO(usernamePasswordDto.getUsername(), usernamePasswordDto.getPassword())
                .orElseThrow(() -> new UserNotFoundException(usernamePasswordDto.getUsername()));
        return jwtService.updateRefreshTokenByUsername(user.getUsername());
    }

    @PostMapping("/token")
    public TokensDto refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        return jwtService.updateRefreshTokenByToken(refreshTokenDto.getRefreshToken());
    }
}
