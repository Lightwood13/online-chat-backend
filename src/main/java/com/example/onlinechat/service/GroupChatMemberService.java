package com.example.onlinechat.service;

import com.example.onlinechat.model.ChatMember;
import com.example.onlinechat.model.keys.UserGroupChatPrimaryKey;
import com.example.onlinechat.repository.ChatMemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
public class GroupChatMemberService {

    private final ChatMemberRepository chatMemberRepository;

    public GroupChatMemberService(ChatMemberRepository chatMemberRepository) {
        this.chatMemberRepository = chatMemberRepository;
    }

    private void checkAdminOrThrow(UUID groupChatId, UUID userId) {
        chatMemberRepository.findById(new UserGroupChatPrimaryKey(userId, groupChatId))
                .filter(chatMember -> chatMember.getRole().equals(ChatMember.Role.admin))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN,
                            "You have to be an admin to do this action");
                });
    }

    public void leaveGroupChat(UUID groupChatId, UUID userId) {
        if (chatMemberRepository.removeFromChat(groupChatId, userId) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group chat or user not found");
        }
    }

    public void kickUser(UUID groupChatId, UUID adminId, UUID userId) {
        if (chatMemberRepository.removeFromChat(groupChatId, userId) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group chat or user not found");
        }
        checkAdminOrThrow(groupChatId, adminId);
    }

    public void promoteUser(UUID groupChatId, UUID adminId, UUID userId) {
        if (chatMemberRepository.promoteUser(groupChatId, userId) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group chat or user not found");
        }
        checkAdminOrThrow(groupChatId, adminId);
    }

    public void demoteUser(UUID groupChatId, UUID adminId, UUID userId) {
        if (chatMemberRepository.demoteUser(groupChatId, userId) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group chat or user not found");
        }
        checkAdminOrThrow(groupChatId, adminId);
    }
}
