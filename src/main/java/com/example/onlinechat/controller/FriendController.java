package com.example.onlinechat.controller;

import com.example.onlinechat.service.FriendService;
import com.example.onlinechat.service.NotificationService;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class FriendController {

    private final FriendService friendService;
    private final NotificationService notificationService;

    public FriendController(FriendService friendService, NotificationService notificationService) {
        this.friendService = friendService;
        this.notificationService = notificationService;
    }

    @GetMapping("/my-friends")
    public List<UserDTO> myFriends(Authentication authentication) {
        return friendService.getFriendsByUserId(UUID.fromString(authentication.getName()));
    }

    @PostMapping("/friend/remove/{friendId}")
    public void removeFriend(Authentication authentication, @PathVariable("friendId") UUID friendId) {
        if (!friendService.removeFriend(UUID.fromString(authentication.getName()), friendId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not friends");
        }
        notificationService.notifyAboutFriendListUpdate(friendId);
    }

    @GetMapping("/friend/request/pending")
    public List<UserDTO> getPendingFriendRequests(Authentication authentication) {
        return friendService.getPendingFriendRequestsByUserId(UUID.fromString(authentication.getName()));
    }

    @PostMapping("/friend/request/send/{toUsername}")
    public void sendFriendRequest(Authentication authentication, @PathVariable("toUsername") String toUsername){
        final UUID toId = friendService.sendFriendRequest(UUID.fromString(authentication.getName()), toUsername);
        notificationService.notifyAboutFriendListUpdate(toId);
    }

    @PostMapping("/friend/request/accept/{fromId}")
    public void acceptFriendRequest(Authentication authentication, @PathVariable("fromId") UUID fromId){
        friendService.acceptFriendRequest(fromId, UUID.fromString(authentication.getName()));
        notificationService.notifyAboutFriendListUpdate(fromId);
    }

    @PostMapping("/friend/request/reject/{fromId}")
    public void declineFriendRequest(Authentication authentication, @PathVariable("fromId") UUID fromId){
        friendService.declineFriendRequest(fromId, UUID.fromString(authentication.getName()));
        notificationService.notifyAboutFriendListUpdate(fromId);
    }
}
