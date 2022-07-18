package com.example.onlinechat.service.dto;

import com.example.onlinechat.model.ChatMember;

import java.util.UUID;

public record MemberDTO(UUID id, ChatMember.Role role) {
    public static MemberDTO fromChatMember(ChatMember chatMember) {
        return new MemberDTO(
                chatMember.getId().getUserId(),
                chatMember.getRole()
        );
    }
}
