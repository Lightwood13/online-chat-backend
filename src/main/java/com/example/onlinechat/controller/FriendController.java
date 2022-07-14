package com.example.onlinechat.controller;

import com.example.onlinechat.exception.FriendNotFoundException;
import com.example.onlinechat.service.FriendService;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/my-friends")
    public List<UserDTO> myFriends(Authentication authentication) {
        return friendService.getFriendsByUserId(UUID.fromString(authentication.getName()));
    }

    @PostMapping("/remove-friend/{friendId}")
    public void removeFriend(Authentication authentication, @PathVariable("friendId") UUID friendId) {
        if (!friendService.removeFriend(UUID.fromString(authentication.getName()), friendId)) {
            throw new FriendNotFoundException();
        }
    }
}
