package com.example.onlinechat.controller;

import com.example.onlinechat.service.AuthenticationService;
import com.example.onlinechat.service.GroupChatService;
import com.example.onlinechat.service.NotificationService;
import com.example.onlinechat.service.dto.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/chats")
public class ChatController {

    private final GroupChatService groupChatService;
    private final NotificationService notificationService;

    ChatController(
            GroupChatService groupChatService,
            NotificationService notificationService) {
        this.groupChatService = groupChatService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<GroupChatWithLastMessageDTO> chats(Authentication authentication) {
        return groupChatService.findGroupChatsWithLastMessageByMemberId(UUID.fromString(authentication.getName()));
    }

    @GetMapping("/{groupChatId}/info")
    public GroupChatDTO chatInfo(@PathVariable UUID groupChatId, Authentication authentication) {
        return groupChatService.getByIdOrThrow(groupChatId, UUID.fromString(authentication.getName()));
    }

    @GetMapping("/{groupChatId}/messages")
    public List<MessageDTO> chatMessages(@PathVariable UUID groupChatId, Authentication authentication) {
        return groupChatService.getMessages(groupChatId, UUID.fromString(authentication.getName()));
    }

    @PostMapping("/{groupChatId}/messages")
    public void sendMessage(@PathVariable UUID groupChatId, @RequestBody NewMessageDTO message, Authentication authentication) {
        final MessageDTO savedMessage = groupChatService.saveNewMessage(
                UUID.fromString(authentication.getName()),
                groupChatId,
                message.text());
        notificationService.notifyAboutNewMessage(savedMessage);
    }

    @PutMapping(value = "/{groupChatId}/profile-photo")
    public FileLocationDTO uploadChatProfilePhoto(
            @PathVariable UUID groupChatId,
            @RequestParam MultipartFile file,
            Authentication authentication) throws Exception {
        final UUID userId = UUID.fromString(authentication.getName());
        final FileLocationDTO result = groupChatService.updateProfilePhoto(
                groupChatId,
                userId,
                file.getBytes(),
                Objects.requireNonNull(file.getOriginalFilename()));

        final GroupChatDTO updatedGroupChat = groupChatService.getByIdOrThrow(groupChatId, userId);
        notificationService.notifyAboutGroupChatProfileUpdate(updatedGroupChat);

        return result;
    }

    @PostMapping
    void createNewGroupChat(@RequestBody NewGroupChatDTO chat) {
        final GroupChatDTO newGroupChat = groupChatService.createNewGroupChat(
                chat.name(),
                chat.members(),
                AuthenticationService.getCurrentUserId());
        notificationService.notifyAboutGroupChatProfileUpdate(newGroupChat);
    }
}
