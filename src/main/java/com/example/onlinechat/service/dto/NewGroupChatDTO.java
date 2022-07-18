package com.example.onlinechat.service.dto;

import java.util.List;
import java.util.UUID;

public record NewGroupChatDTO(String name, List<UUID> members) {
}
