package com.example.onlinechat.repository;

import com.example.onlinechat.model.GroupChat;
import com.example.onlinechat.service.dto.GroupChatWithLastMessageDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupChatRepository extends CrudRepository<GroupChat, UUID> {
    @Query(value = "SELECT DISTINCT ON (gc.id)" +
            " cast(gc.id AS varchar)       AS id," +
            " gc.name                      AS name," +
            " gc.profile_photo_location    AS profilePhotoLocation," +
            " gc.created_on                AS createdOn,  " +
            " cast(m.id AS varchar)        AS lastMessageId," +
            " cast(m.author_id AS varchar) AS lastMessageAuthorId," +
            " m.text                       AS lastMessageText," +
            " m.sent_on                    AS lastMessageSentOn" +
            "      FROM group_chat  gc" +
            "      JOIN chat_member cm ON gc.id = cm.group_chat_id" +
            " LEFT JOIN message     m  ON gc.id = m.group_chat_id" +
            " WHERE cast(cm.user_id AS varchar) = :id" +
            " ORDER BY gc.id, sent_on DESC",
            nativeQuery = true)
    List<GroupChatWithLastMessageDTO> findGroupChatsWithLastMessageByMemberId(@Param("id") String id);

    @Query("SELECT gc FROM GroupChat gc JOIN gc.members m" +
            " WHERE gc.id = :group_chat_id AND m.user.id = :user_id")
    Optional<GroupChat> confirmAccessAndGetById(@Param("group_chat_id") UUID groupChatId, @Param("user_id") UUID userId);
}
