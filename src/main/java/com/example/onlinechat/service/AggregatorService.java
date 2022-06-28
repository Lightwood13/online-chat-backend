package com.example.onlinechat.service;

import com.example.onlinechat.model.User;
import com.example.onlinechat.service.dto.GroupChatDTO;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AggregatorService {

    final UserService userService;
    final MessageService messageService;

    public AggregatorService(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    public Set<GroupChatDTO> getChatsForUser(String username) {
        final User user = userService.getUserByUsernameOrThrow(username);
        return user
                .getGroupChats().stream()
                .map(groupChat -> new GroupChatDTO(
                        groupChat.getId(),
                        groupChat.getName(),
                        messageService.getLastMessageForGroupChat(groupChat)
                ))
                .collect(Collectors.toSet());
    }
}
