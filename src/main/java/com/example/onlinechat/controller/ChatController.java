package com.example.onlinechat.controller;

import com.example.onlinechat.model.Message;
import com.example.onlinechat.service.AggregatorService;
import com.example.onlinechat.service.MessageService;
import com.example.onlinechat.service.dto.GroupChatDTO;
import com.example.onlinechat.service.dto.MessageDTO;
import com.example.onlinechat.service.dto.NewMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final AggregatorService aggregatorService;

    ChatController(MessageService messageService, AggregatorService aggregatorService) {
        this.messageService = messageService;
        this.aggregatorService = aggregatorService;
    }

    @CrossOrigin
    @GetMapping("/chat/{groupChatId}")
    public @ResponseBody List<MessageDTO> chat(@PathVariable UUID groupChatId, Authentication authentication) {
        return messageService.getAllMessagesForGroupChat(groupChatId, authentication.getName());
    }

    @CrossOrigin
    @GetMapping("/chats")
    public @ResponseBody Set<GroupChatDTO> chats(Authentication authentication) {
        return aggregatorService.getChatsForUser(authentication.getName());
    }

    @MessageMapping("/send")
    @SendTo("/messages/new")
    public MessageDTO sendMessage(NewMessageDTO message, Authentication authentication) {
        final Message savedMessage = messageService
                .save(authentication.getName(), message.groupChatId(), message.text());
        return new MessageDTO(
                savedMessage.getId(),
                savedMessage.getAuthor().getName(),
                message.text(),
                savedMessage.getSentOn()
        );
    }
}
