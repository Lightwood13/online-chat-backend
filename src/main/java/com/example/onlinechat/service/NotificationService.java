package com.example.onlinechat.service;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.User;
import com.example.onlinechat.service.dto.GroupChatDTO;
import com.example.onlinechat.service.dto.MessageDTO;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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

    public void notifyAboutProfileUpdate(UUID recipientId, User updatedUser) {
        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/profile-updates",
                UserDTO.fromUser(updatedUser)
        );
    }

    public void notifyAboutGroupChatProfileUpdate(
            UUID recipientId,
            GroupChat updatedGroupChat
    ) {
        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/group-chat-profile-updates",
                GroupChatDTO.fromGroupChat(updatedGroupChat)
        );
    }
}
