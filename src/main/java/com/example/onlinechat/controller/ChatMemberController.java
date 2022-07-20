package com.example.onlinechat.controller;

import com.example.onlinechat.service.GroupChatMemberService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/chats")
public class ChatMemberController {

    private final GroupChatMemberService groupChatMemberService;

    public ChatMemberController(GroupChatMemberService groupChatMemberService) {
        this.groupChatMemberService = groupChatMemberService;
    }

    @DeleteMapping("/{group-chat-id}/users/me")
    void leaveGroupChat(@PathVariable("group-chat-id") UUID groupChatId, Authentication authentication) {
        groupChatMemberService.leaveGroupChat(groupChatId, UUID.fromString(authentication.getName()));
    }

    @DeleteMapping("/{group-chat-id}/users/{user-id}")
    void kickUser(
            @PathVariable("group-chat-id") UUID groupChatId,
            @PathVariable("user-id") UUID userId,
            Authentication authentication) {
        groupChatMemberService.kickUser(groupChatId, UUID.fromString(authentication.getName()), userId);
    }

    @PutMapping("/{group-chat-id}/admins/{user-id}")
    void promoteUser(
            @PathVariable("group-chat-id") UUID groupChatId,
            @PathVariable("user-id") UUID userId,
            Authentication authentication) {
        groupChatMemberService.promoteUser(groupChatId, UUID.fromString(authentication.getName()), userId);
    }

    @DeleteMapping("/{group-chat-id}/admins/{user-id}")
    void demoteUser(
            @PathVariable("group-chat-id") UUID groupChatId,
            @PathVariable("user-id") UUID userId,
            Authentication authentication) {
        groupChatMemberService.demoteUser(groupChatId, UUID.fromString(authentication.getName()), userId);
    }
}
