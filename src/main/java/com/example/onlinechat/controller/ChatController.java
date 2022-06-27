package com.example.onlinechat.controller;

import com.example.onlinechat.model.Message;
import com.example.onlinechat.service.MessageService;
import com.example.onlinechat.service.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChatController {

    private final MessageService messageService;

    ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @CrossOrigin
    @GetMapping("/chat")
    public @ResponseBody List<MessageDTO> chat() {
        return messageService.getAllMessages();
    }

    @MessageMapping("/send")
    @SendTo("/messages/new")
    public MessageDTO sendMessage(String message, Authentication authentication) {
        final Message savedMessage = messageService.save(authentication.getName(), message);
        return new MessageDTO(
                savedMessage.getId(),
                savedMessage.getAuthor().getName(),
                message,
                savedMessage.getSentOn()
        );
    }
}
