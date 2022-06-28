package com.example.onlinechat.service.dto;

import com.example.onlinechat.model.Message;

import java.sql.Timestamp;
import java.util.UUID;

public record MessageDTO(UUID id, String authorName, String text, Timestamp sentOn) {
    public static MessageDTO fromMessage(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getAuthor().getName(),
                message.getText(),
                message.getSentOn()
        );
    }
}
