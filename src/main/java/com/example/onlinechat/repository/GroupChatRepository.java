package com.example.onlinechat.repository;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.service.dto.GroupChatWithLastMessageDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupChatRepository extends CrudRepository<GroupChat, UUID> {
    @Query(value = "SELECT DISTINCT ON (gc.id)" +
            " cast(gc.id AS varchar)       AS id," +
            " gc.name                      AS name," +
            " gc.profile_photo_location    AS profilePhotoLocation," +
            " cast(m.id AS varchar)        AS lastMessageId," +
            " cast(m.author_id AS varchar) AS lastMessageAuthorId," +
            " m.text                       AS lastMessageText," +
            " m.sent_on                    AS lastMessageSentOn" +
            " FROM group_chat gc" +
            " JOIN user_group_chat ugc ON gc.id = ugc.group_chat_id" +
            " JOIN message m           ON gc.id = m.group_chat_id" +
            " WHERE cast(ugc.user_id AS varchar) = :id" +
            " ORDER BY gc.id, sent_on DESC",
            nativeQuery = true)
    List<GroupChatWithLastMessageDTO> findGroupChatsWithLastMessageByMemberId(@Param("id") String id);
}
