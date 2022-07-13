package com.example.onlinechat.controller;

import com.example.onlinechat.service.FriendService;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @CrossOrigin
    @GetMapping("/my-friends")
    public List<UserDTO> myFriends(Authentication authentication) {
        return friendService.getFriendsByUserUsername(authentication.getName());
    }
}
