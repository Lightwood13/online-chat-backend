package com.example.onlinechat.service.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record MessageDTO(UUID id, String authorName, String text, Timestamp sentOn) {
}
