package com.example.onlinechat.controller;

import com.example.onlinechat.service.GroupChatService;
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
    private final GroupChatService groupChatService;
    private final NotificationService notificationService;

    public UserController(
            UserService userService,
            GroupChatService groupChatService,
            NotificationService notificationService) {
        this.userService = userService;
        this.groupChatService = groupChatService;
        this.notificationService = notificationService;
    }

    @CrossOrigin
    @GetMapping("/my-profile-info")
    public UserDTO myInfo(Authentication authentication) {
        return userService.getByUsernameOrThrow(authentication.getName());
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

        final UserDTO updatedUser = userService.getByUsernameOrThrow(authentication.getName());
        notificationService.notifyAboutProfileUpdate(
                groupChatService.findUserIdsThatShareGroupChatWith(updatedUser.id()),
                updatedUser
        );

        return result;
    }
}