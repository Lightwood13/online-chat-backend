package com.example.onlinechat.service.dto;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record GroupChatWithMembersAndMessagesDTO(
        UUID id,
        String name,
        String profilePhotoLocation,
        Set<UserDTO> members,
        List<MessageDTO> messages
) {

    public GroupChatWithMembersAndMessagesDTO(
            GroupChat groupChat,
            Set<User> members,
            List<MessageDTO> messages) {
        this(groupChat.getId(),
                groupChat.getName(),
                groupChat.getProfilePhotoLocation(),
                members.stream()
                        .map(UserDTO::fromUser)
                        .collect(Collectors.toSet()),
                messages);
    }
}
