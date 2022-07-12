package com.example.onlinechat.controller;

import com.example.onlinechat.model.User;
import com.example.onlinechat.service.NotificationService;
import com.example.onlinechat.service.UserService;
import com.example.onlinechat.service.dto.FileLocationDTO;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;

    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @CrossOrigin
    @GetMapping("/my-profile-info")
    public UserDTO myInfo(Authentication authentication) {
        return UserDTO.fromUser(userService.getUserByUsernameOrThrow(authentication.getName()));
    }

    @CrossOrigin
    @GetMapping("/profile-info")
    public List<UserDTO> profileInfo(@RequestParam List<UUID> ids, String userId) {
        return userService.findUsersByIdIn(ids);
    }

    @CrossOrigin
    @PostMapping("/profile-photo")
    public FileLocationDTO uploadProfilePhoto(
            @RequestParam MultipartFile file,
            Authentication authentication
    ) throws Exception {
        final FileLocationDTO result = userService.updateProfilePhoto(
                authentication.getName(),
                file.getBytes(),
                Objects.requireNonNull(file.getOriginalFilename())
        );

        final User updatedUser = userService.getUserByUsernameOrThrow(authentication.getName());
        updatedUser
                .getGroupChats().stream()
                .flatMap(groupChat -> groupChat.getMembers().stream())
                .distinct()
                .forEach(user -> notificationService.notifyAboutProfileUpdate(user.getId(), updatedUser));

        return result;
    }
}
