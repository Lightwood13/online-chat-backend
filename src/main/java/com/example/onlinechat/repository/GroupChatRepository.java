package com.example.onlinechat.repository;

import com.example.onlinechat.model.GroupChat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupChatRepository extends CrudRepository<GroupChat, UUID> {
}
