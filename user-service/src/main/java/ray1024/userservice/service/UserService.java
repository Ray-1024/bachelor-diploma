package ray1024.userservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ray1024.userservice.exception.UserAlreadyExistsException;
import ray1024.userservice.model.entity.User;
import ray1024.userservice.repository.UserRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsernameO(@NonNull String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return findByUsernameO(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<User> findByUsernameAndPasswordO(@NonNull String username, @NonNull String password) {
        Optional<User> userO = findByUsernameO(username);
        if (userO.isEmpty()) return Optional.empty();
        if (!passwordEncoder.matches(password, userO.get().getPassword())) return Optional.empty();
        return userO;
    }

    public User findByUsernameAndPassword(@NonNull String username, @NonNull String password) {
        return findByUsernameAndPasswordO(username, password).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User create(@NonNull String username, @NonNull String password) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException(username);
        }
        return userRepository.save(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .build()
        );
    }
}
