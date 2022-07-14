package com.example.onlinechat.service;

import com.example.onlinechat.repository.FriendRepository;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FriendService {
    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public List<UserDTO> getFriendsByUserId(UUID userId) {
        return friendRepository.getFriendsByUserId(userId.toString())
                .stream().map(UserDTO::fromUserProjection).toList();
    }

    public boolean removeFriend(UUID userId, UUID friendId) {
        return friendRepository.removeFriend(userId.toString(), friendId.toString()) > 0;
    }
}
