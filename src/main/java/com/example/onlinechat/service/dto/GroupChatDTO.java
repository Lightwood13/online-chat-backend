package com.example.onlinechat.service.dto;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record GroupChatDTO(
        UUID id,
        String name,
        String profilePhotoLocation,
        Set<UUID> members
) {
    public static GroupChatDTO fromGroupChat(GroupChat groupChat) {
        return new GroupChatDTO(
                groupChat.getId(),
                groupChat.getName(),
                groupChat.getProfilePhotoLocation(),
                groupChat.getMembers().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );
    }
}
