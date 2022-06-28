package com.example.onlinechat.service;

import com.example.onlinechat.exception.ForbiddenException;
import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.User;
import com.example.onlinechat.repository.GroupChatRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;

    private final UserService userService;

    public GroupChatService(
            GroupChatRepository groupChatRepository,
            UserService userService
    ) {
        this.groupChatRepository = groupChatRepository;
        this.userService = userService;
    }

    public GroupChat getByIdOrThrow(UUID id) {
        return groupChatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group chat not found"));
    }

    @Transactional
    public void confirmAccessOrThrow(UUID groupChatId, String username) {
        final User user = userService.getUserByUsernameOrThrow(username);
        final GroupChat groupChat = getByIdOrThrow(groupChatId);

        if (!groupChat.getMembers().contains(user))
            throw new ForbiddenException();
    }
}
