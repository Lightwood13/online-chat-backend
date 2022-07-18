package com.example.onlinechat.service;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.Message;
import com.example.onlinechat.model.User;
import com.example.onlinechat.repository.MessageRepository;
import com.example.onlinechat.service.dto.MessageDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageDTO> findAllByGroupChatIdOrderBySentOn(UUID groupChatId) {
        return messageRepository.findAllByGroupChatIdOrderBySentOn(groupChatId)
                .stream()
                .map(MessageDTO::fromMessage)
                .toList();
    }

    public MessageDTO save(UUID groupChatId, UUID userId, String text) {
        final Message savedMessage = messageRepository.save(Message.builder()
                .groupChat(GroupChat.withId(groupChatId))
                .author(User.withId(userId))
                .text(text)
                .sentOn(Timestamp.from(Instant.now()))
                .build()
        );
        return MessageDTO.fromMessage(savedMessage);
    }
}
