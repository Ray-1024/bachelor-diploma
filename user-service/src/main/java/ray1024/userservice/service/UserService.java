package ray1024.userservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ray1024.userservice.exception.UserAlreadyExistsException;
import ray1024.userservice.exception.UserNotFoundException;
import ray1024.userservice.model.entity.UserEntity;
import ray1024.userservice.repository.UserRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return findByUsernameO(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<UserEntity> findByUsernameO(@NonNull String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity findByUsername(@NonNull String username) throws UsernameNotFoundException {
        return findByUsernameO(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public Optional<UserEntity> findByUsernameAndPasswordO(@NonNull String username, @NonNull String password) {
        return userRepository.findByUsernameAndPassword(username, passwordEncoder.encode(password));
    }

    public UserEntity findByUsernameAndPassword(@NonNull String username, @NonNull String password) {
        return findByUsernameAndPasswordO(username, password).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserEntity create(@NonNull String username, @NonNull String password) throws UserAlreadyExistsException {
        if (findByUsernameAndPasswordO(username, password).isPresent()) {
            throw new UserAlreadyExistsException(username);
        }
        return userRepository.save(
                UserEntity.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .build()
        );
    }

    public UserEntity update(@NonNull UserEntity user) throws UserNotFoundException {
        if (findByUsernameAndPasswordO(user.getUsername(), user.getPassword()).isEmpty()) {
            throw new UserNotFoundException(user.getUsername());
        }
        return userRepository.save(user);
    }

    public void delete(@NonNull UserEntity user) throws UserNotFoundException {
        if (findByUsernameAndPasswordO(user.getUsername(), user.getPassword()).isEmpty()) {
            throw new UserNotFoundException(user.getUsername());
        }
        userRepository.delete(user);
    }
}
