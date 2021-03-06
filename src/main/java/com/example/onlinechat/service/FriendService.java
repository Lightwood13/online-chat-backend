package com.example.onlinechat.service;

import com.example.onlinechat.model.Friend;
import com.example.onlinechat.model.User;
import com.example.onlinechat.model.keys.FriendPrimaryKey;
import com.example.onlinechat.repository.FriendRepository;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FriendService {
    private final UserService userService;
    private final FriendRepository friendRepository;

    public FriendService(UserService userService, FriendRepository friendRepository) {
        this.userService = userService;
        this.friendRepository = friendRepository;
    }

    public List<UserDTO> getFriendsByUserId(UUID userId) {
        return friendRepository.getFriendsByUserId(userId)
                .stream().map(UserDTO::fromUser).toList();
    }

    public List<UserDTO> getPendingFriendRequestsByUserId(UUID userId) {
        return friendRepository.getPendingFriendRequestsByUserId(userId)
                .stream().map(UserDTO::fromUser).toList();
    }

    public UUID sendFriendRequest(UUID from, String toUsername) {
        final UserDTO toUser = userService.findUserByUsernameOrThrow(toUsername)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
        if (from.equals(toUser.id())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot send friend request to yourself"
            );
        }
        friendRepository.findById(new FriendPrimaryKey(from, toUser.id()))
                .ifPresent(friend -> {
                    if (friend.isPending()) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "You already have a pending request to this user");
                    } else {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "You are already friends");
                    }
                });
        friendRepository.findById(new FriendPrimaryKey(toUser.id(), from))
                .ifPresent(friend -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "You already have a pending request from this user"
                    );
                });
        friendRepository.save(new Friend(new FriendPrimaryKey(), User.withId(from), User.withId(toUser.id()), true));
        return toUser.id();
    }

    public void acceptFriendRequest(UUID from, UUID to) {
        final Friend friendRequest = friendRepository.findById(new FriendPrimaryKey(from, to))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Friend request not found"));

        if (!friendRequest.isPending()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already friends");
        }
        friendRequest.setPending(false);
        friendRepository.save(new Friend(new FriendPrimaryKey(), User.withId(to), User.withId(from), false));
    }

    public void declineFriendRequest(UUID from, UUID to) {
        final Friend friendRequest = friendRepository.findById(new FriendPrimaryKey(from, to))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Friend request not found"));

        if (!friendRequest.isPending()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already friends");
        }
        friendRepository.delete(friendRequest);
    }

    public void removeFriend(UUID userId, UUID friendId) {
        if (friendRepository.removeFriend(userId, friendId) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not friends");
        }
    }
}
