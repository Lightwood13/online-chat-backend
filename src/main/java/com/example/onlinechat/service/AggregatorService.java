package com.example.onlinechat.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AggregatorService {

    final UserService userService;
    final GroupChatService groupChatService;
    final MessageService messageService;

    public AggregatorService(
            UserService userService,
            GroupChatService groupChatService,
            MessageService messageService) {
        this.userService = userService;
        this.groupChatService = groupChatService;
        this.messageService = messageService;
    }


}
