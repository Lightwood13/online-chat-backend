package com.example.onlinechat.controller;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.service.GroupChatService;
import com.example.onlinechat.service.NotificationService;
import com.example.onlinechat.service.dto.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class ChatController {

    private final GroupChatService groupChatService;
    private final NotificationService notificationService;

    ChatController(
            GroupChatService groupChatService,
            NotificationService notificationService) {
        this.groupChatService = groupChatService;
        this.notificationService = notificationService;
    }

    @CrossOrigin
    @GetMapping("/chats")
    public List<GroupChatWithLastMessageDTO> chats(Authentication authentication) {
        return groupChatService.findGroupChatsWithLastMessageByMemberUsername(authentication.getName());
    }

    @CrossOrigin
    @GetMapping("/chat/{groupChatId}")
    public GroupChatWithMembersAndMessagesDTO chat(@PathVariable UUID groupChatId, Authentication authentication) {
        return groupChatService.getGroupChatWithMembersAndMessagesById(groupChatId, authentication.getName());
    }

    @CrossOrigin
    @PostMapping("/send")
    public void sendMessage(@RequestBody NewMessageDTO message, Authentication authentication) {
        final MessageDTO savedMessage = groupChatService.saveNewMessage(
                authentication.getName(),
                message.groupChatId(),
                message.text());
        notificationService.notifyAboutNewMessage(savedMessage);
    }

    @CrossOrigin
    @PostMapping("/group-chat-profile-photo/{group-chat-id}")
    public FileLocationDTO uploadChatProfilePhoto(
            @PathVariable("group-chat-id") UUID groupChatId,
            @RequestParam MultipartFile file,
            Authentication authentication) throws Exception {
        final FileLocationDTO result = groupChatService.updateProfilePhoto(
                groupChatId,
                authentication.getName(),
                file.getBytes(),
                Objects.requireNonNull(file.getOriginalFilename())
        );

        final GroupChat updatedGroupChat = groupChatService.getByIdOrThrow(groupChatId);
        updatedGroupChat
                .getMembers()
                .forEach(user -> notificationService.notifyAboutGroupChatProfileUpdate(
                        user.getId(),
                        updatedGroupChat
                ));

        return result;
    }
}
