package com.example.onlinechat.service.dto;

import com.example.onlinechat.model.GroupChat;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record GroupChatDTO(
        UUID id,
        String name,
        String profilePhotoLocation,
        Timestamp createdOn,
        Set<MemberDTO> members
) {
    public static GroupChatDTO fromGroupChat(GroupChat groupChat) {
        return new GroupChatDTO(
                groupChat.getId(),
                groupChat.getName(),
                groupChat.getProfilePhotoLocation(),
                groupChat.getCreatedOn(),
                groupChat.getMembers().stream()
                        .map(MemberDTO::fromChatMember)
                        .collect(Collectors.toSet())
        );
    }
}
