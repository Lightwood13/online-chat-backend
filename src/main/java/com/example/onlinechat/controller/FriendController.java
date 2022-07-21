package com.example.onlinechat.controller;

import com.example.onlinechat.service.FriendService;
import com.example.onlinechat.service.NotificationService;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;
    private final NotificationService notificationService;

    public FriendController(FriendService friendService, NotificationService notificationService) {
        this.friendService = friendService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<UserDTO> myFriends(Authentication authentication) {
        return friendService.getFriendsByUserId(UUID.fromString(authentication.getName()));
    }

    @DeleteMapping("/{friendId}")
    public void removeFriend(Authentication authentication, @PathVariable("friendId") UUID friendId) {
        friendService.removeFriend(UUID.fromString(authentication.getName()), friendId);
        notificationService.notifyAboutFriendListUpdate(friendId);
    }

    @GetMapping("/requests")
    public List<UserDTO> getPendingFriendRequests(Authentication authentication) {
        return friendService.getPendingFriendRequestsByUserId(UUID.fromString(authentication.getName()));
    }

    @PostMapping("/requests/{toUsername}")
    public void sendFriendRequest(Authentication authentication, @PathVariable("toUsername") String toUsername){
        final UUID toId = friendService.sendFriendRequest(UUID.fromString(authentication.getName()), toUsername);
        notificationService.notifyAboutFriendListUpdate(toId);
    }

    @PatchMapping("/requests/{fromId}")
    public void acceptFriendRequest(Authentication authentication, @PathVariable("fromId") UUID fromId){
        friendService.acceptFriendRequest(fromId, UUID.fromString(authentication.getName()));
        notificationService.notifyAboutFriendListUpdate(fromId);
    }

    @DeleteMapping("/requests/{fromId}")
    public void declineFriendRequest(Authentication authentication, @PathVariable("fromId") UUID fromId){
        friendService.declineFriendRequest(fromId, UUID.fromString(authentication.getName()));
        notificationService.notifyAboutFriendListUpdate(fromId);
    }
}
