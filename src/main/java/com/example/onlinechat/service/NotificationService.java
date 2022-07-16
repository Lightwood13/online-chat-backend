package com.example.onlinechat.service;

import com.example.onlinechat.service.dto.GroupChatDTO;
import com.example.onlinechat.service.dto.MessageDTO;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    final private SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyAboutNewMessage(MessageDTO message) {
        messagingTemplate.convertAndSend("/messages/new", message);
    }

    public void notifyAboutProfileUpdate(List<UUID> recipientIds, UserDTO updatedUser) {
        for (UUID recipientId: recipientIds) {
            messagingTemplate.convertAndSendToUser(
                    recipientId.toString(),
                    "/profile-updates",
                    updatedUser
            );
        }
    }

    public void notifyAboutGroupChatProfileUpdate(GroupChatDTO updatedGroupChat) {
        for (UUID memberId: updatedGroupChat.members()) {
            messagingTemplate.convertAndSendToUser(
                    memberId.toString(),
                    "/group-chat-profile-updates",
                    updatedGroupChat
            );
        }
    }

    public void notifyAboutFriendListUpdate(UUID userId) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/friend-list-updates",
                "Friend list updated"
        );
    }
}
