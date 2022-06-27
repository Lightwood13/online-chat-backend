package com.example.onlinechat.service;

import com.example.onlinechat.model.Message;
import com.example.onlinechat.model.User;
import com.example.onlinechat.repository.MessageRepository;
import com.example.onlinechat.repository.UserRepository;
import com.example.onlinechat.service.dto.MessageDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageDTO> getAllMessages() {
        Iterable<Message> allMessages = messageRepository.findAll();
        return StreamSupport.stream(allMessages.spliterator(), false)
                .map(message -> new MessageDTO(
                        message.getId(),
                        message.getAuthor().getName(),
                        message.getText(),
                        message.getSentOn()
                ))
                .collect(Collectors.toList());
    }

    public Message save(String username, String message) {
        final Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty())
            throw new IllegalArgumentException("User not found");
        return messageRepository.save(Message.builder()
                .author(user.get())
                .text(message)
                .sentOn(Timestamp.from(Instant.now()))
                .build()
        );
    }
}
