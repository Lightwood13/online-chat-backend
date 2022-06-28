package com.example.onlinechat.service.dto;

import java.util.UUID;

public record NewMessageDTO(String text, UUID groupChatId) {
}
