package com.example.onlinechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    @CrossOrigin
    @GetMapping("/chat")
    public @ResponseBody List<Message> chat() {
        Iterable<Message> allMessages = messageRepository.findAll();
        return StreamSupport.stream(allMessages.spliterator(), false)
                .collect(Collectors.toList());
    }

    @MessageMapping("/send")
    @SendTo("/messages/new")
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }
}
