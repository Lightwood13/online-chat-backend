package com.example.onlinechat.service;

import com.auth0.jwt.JWT;
import com.example.onlinechat.config.SecurityConfig;
import com.example.onlinechat.exception.InvalidCredentialsException;
import com.example.onlinechat.exception.UserAlreadyExistsException;
import com.example.onlinechat.model.User;
import com.example.onlinechat.repository.UserRepository;
import com.example.onlinechat.service.dto.LoginCredentialsDTO;
import com.example.onlinechat.service.dto.SignUpCredentialsDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User getUserByUsernameOrThrow(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public String loginUser(LoginCredentialsDTO credentials) {
        if (credentials.username() == null || credentials.password() == null)
            throw new InvalidCredentialsException();

        userRepository.findUserByUsername(credentials.username())
                .map(User::getEncryptedPassword)
                .filter(password -> passwordEncoder.matches(credentials.password(), password))
                .orElseThrow(InvalidCredentialsException::new);

        return JWT.create() // TODO: add expiration date
                .withSubject(credentials.username())
                .sign(SecurityConfig.JWT_SIGNING_ALGORITHM);
    }

    public String signupUser(SignUpCredentialsDTO credentials) {
        if (credentials.username() == null || credentials.name() == null || credentials.password() == null)
            throw new InvalidCredentialsException();

        final User user = userRepository
                .findUserByUsername(credentials.username())
                .orElseThrow(UserAlreadyExistsException::new);

        final User newUser = User.builder()
                .username(credentials.username())
                .name(credentials.name())
                .encryptedPassword(passwordEncoder.encode(credentials.password()))
                .build();
        userRepository.save(newUser);

        return JWT.create()
                .withSubject(credentials.username())
                .sign(SecurityConfig.JWT_SIGNING_ALGORITHM);
    }
}
