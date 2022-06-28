package com.example.onlinechat.repository;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<Message, UUID> {
    Iterable<Message> findAllByGroupChatOrderBySentOn(GroupChat groupChat);

    Optional<Message> findTopByGroupChatOrderBySentOnDesc(GroupChat groupChat);
}
