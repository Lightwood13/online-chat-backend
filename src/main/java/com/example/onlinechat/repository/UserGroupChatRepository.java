package com.example.onlinechat.repository;

import com.example.onlinechat.model.UserGroupChat;
import com.example.onlinechat.model.UserGroupChatKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserGroupChatRepository extends CrudRepository<UserGroupChat, UserGroupChatKey> {
    boolean existsByUserUsernameAndGroupChatId(String username, UUID groupChatId);
}
