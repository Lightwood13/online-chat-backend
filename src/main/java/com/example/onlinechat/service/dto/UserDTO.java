package com.example.onlinechat.service.dto;

import com.example.onlinechat.model.User;

import java.util.UUID;

public record UserDTO(UUID id, String name) {
    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getName());
    }
}
