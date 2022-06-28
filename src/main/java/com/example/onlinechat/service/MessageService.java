package com.example.onlinechat.service;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.Message;
import com.example.onlinechat.model.User;
import com.example.onlinechat.repository.MessageRepository;
import com.example.onlinechat.service.dto.MessageDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final UserService userService;
    private final GroupChatService groupChatService;

    MessageService(
            MessageRepository messageRepository,
            UserService userService,
            GroupChatService groupChatService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.groupChatService = groupChatService;
    }

    public List<MessageDTO> getAllMessagesForGroupChat(UUID groupChatId, String username) {
        groupChatService.confirmAccessOrThrow(groupChatId, username);

        final GroupChat groupChat = groupChatService.getByIdOrThrow(groupChatId);

        Iterable<Message> allMessages = messageRepository
                .findAllByGroupChatOrderBySentOn(groupChat);

        return StreamSupport.stream(allMessages.spliterator(), false)
                .map(MessageDTO::fromMessage)
                .collect(Collectors.toList());
    }

    public Optional<MessageDTO> getLastMessageForGroupChat(GroupChat groupChat) {
        return messageRepository.findTopByGroupChatOrderBySentOnDesc(groupChat)
                .map(MessageDTO::fromMessage);
    }

    public Message save(String username, UUID groupChatId, String message) {
        groupChatService.confirmAccessOrThrow(groupChatId, username);

        final User user = userService.getUserByUsernameOrThrow(username);
        final GroupChat groupChat = groupChatService.getByIdOrThrow(groupChatId);

        return messageRepository.save(Message.builder()
                .groupChat(groupChat)
                .author(user)
                .text(message)
                .sentOn(Timestamp.from(Instant.now()))
                .build()
        );
    }
}
