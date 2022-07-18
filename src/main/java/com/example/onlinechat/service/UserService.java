package com.example.onlinechat.service;

import com.auth0.jwt.JWT;
import com.example.onlinechat.config.SecurityConfig;
import com.example.onlinechat.exception.InvalidCredentialsException;
import com.example.onlinechat.exception.UserAlreadyExistsException;
import com.example.onlinechat.model.User;
import com.example.onlinechat.repository.UserRepository;
import com.example.onlinechat.service.dto.FileLocationDTO;
import com.example.onlinechat.service.dto.LoginCredentialsDTO;
import com.example.onlinechat.service.dto.SignUpCredentialsDTO;
import com.example.onlinechat.service.dto.UserDTO;
import com.example.onlinechat.util.Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private static final String DEFAULT_PROFILE_PHOTO_LOCATION = "default.png";

    private final UserRepository userRepository;

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    UserService(
            UserRepository userRepository,
            FileService fileService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
    }

    private User getUserByIdOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Optional<UserDTO> findUserByUsernameOrThrow(String username) {
        return userRepository.findUserByUsername(username).map(UserDTO::fromUser);
    }

    public UserDTO getByIdOrThrow(UUID userId) {
        return UserDTO.fromUser(getUserByIdOrThrow(userId));
    }

    public List<UserDTO> findUsersByIdIn(List<UUID> ids) {
        return userRepository.findUsersByIdIn(ids)
                .stream().map(UserDTO::fromUser)
                .toList();
    }

    public FileLocationDTO updateProfilePhoto(UUID userId, byte[] photo, String originalFilename) throws Exception {
        final String extension = Util.getFileExtension(originalFilename);
        final User user = getUserByIdOrThrow(userId);

        final String location = fileService.saveOrUpdate(
                photo,
                extension,
                user.getProfilePhotoLocation());
        user.setProfilePhotoLocation(location);
        return new FileLocationDTO(location);
    }

    public String loginUser(LoginCredentialsDTO credentials) {
        if (credentials.username() == null || credentials.password() == null)
            throw new InvalidCredentialsException();

        final User user = userRepository.findUserByUsername(credentials.username())
                .filter(u -> passwordEncoder.matches(credentials.password(), u.getEncryptedPassword()))
                .orElseThrow(InvalidCredentialsException::new);

        return JWT.create() // TODO: add expiration date
                .withSubject(user.getId().toString())
                .sign(SecurityConfig.JWT_SIGNING_ALGORITHM);
    }

    public String signupUser(SignUpCredentialsDTO credentials) {
        if (credentials.username() == null || credentials.name() == null || credentials.password() == null)
            throw new InvalidCredentialsException();

        userRepository
                .findUserByUsername(credentials.username())
                .ifPresent(user -> { throw new UserAlreadyExistsException(); });

        final User newUser = User.builder()
                .username(credentials.username())
                .name(credentials.name())
                .encryptedPassword(passwordEncoder.encode(credentials.password()))
                .build();
        userRepository.save(newUser);

        return JWT.create()
                .withSubject(newUser.getId().toString())
                .sign(SecurityConfig.JWT_SIGNING_ALGORITHM);
    }
}
