package com.example.onlinechat.controller;

import com.example.onlinechat.service.FriendService;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @CrossOrigin
    @GetMapping("/my-friends")
    public List<UserDTO> myFriends(Authentication authentication) {
        return friendService.getFriendsByUserId(UUID.fromString(authentication.getName()));
    }
}
