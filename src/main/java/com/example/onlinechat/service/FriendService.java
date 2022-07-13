package com.example.onlinechat.service;

import com.example.onlinechat.repository.FriendRepository;
import com.example.onlinechat.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FriendService {
    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public List<UserDTO> getFriendsByUserUsername(String username) {
        return friendRepository.getFriendsByUserUsername(username)
                .stream().map(UserDTO::fromUserProjection).toList();
    }

}
