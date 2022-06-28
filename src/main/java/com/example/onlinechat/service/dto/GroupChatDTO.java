package com.example.onlinechat.service.dto;

import java.util.Optional;
import java.util.UUID;

public record GroupChatDTO(UUID id, String name, Optional<MessageDTO> lastMessage) {
}
