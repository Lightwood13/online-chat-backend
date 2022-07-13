package com.example.onlinechat.service.dto;

import com.example.onlinechat.model.User;
import com.example.onlinechat.service.projections.UserProjection;

import java.util.UUID;

public record UserDTO(UUID id, String name, String profilePhotoLocation) {
    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getProfilePhotoLocation()
        );
    }

    public static UserDTO fromUserProjection(UserProjection userProjection) {
        return new UserDTO(
                UUID.fromString(userProjection.getId()),
                userProjection.getName(),
                userProjection.getProfilePhotoLocation()
        );
    }
}
