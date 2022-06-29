package com.example.onlinechat.service.dto;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public record GroupChatDTO(
        UUID id,
        String name,
        Set<UserDTO> members,
        Optional<MessageDTO> lastMessage
) {
}
