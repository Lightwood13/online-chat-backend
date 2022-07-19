package com.example.onlinechat.repository;

import com.example.onlinechat.model.ChatMember;
import com.example.onlinechat.model.keys.UserGroupChatPrimaryKey;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMemberRepository extends CrudRepository<ChatMember, UserGroupChatPrimaryKey> {

    // TODO: interchange user and group chat
    boolean existsById_UserIdAndId_GroupChatId(UUID userId, UUID groupChatId);

    @Query("SELECT DISTINCT ugc.user.id" +
            " FROM ChatMember ugc" +
            " WHERE ugc.groupChat.id IN " +
            "   (SELECT ugc2.groupChat.id" +
            "    FROM ChatMember ugc2" +
            "    WHERE ugc2.user.id = :user_id)")
    List<UUID> getUserIdsThatShareGroupChatWith(@Param("user_id") UUID userId);

    // TODO: refactor other queries to use """ and remove param
    @Modifying
    @Query("""
            DELETE FROM ChatMember cm
            WHERE cm.groupChat.id = :group_chat_id AND cm.user.id = :user_id
            """)
    int removeFromChat(@Param("group_chat_id") UUID groupChatId, @Param("user_id") UUID userId);


    @Modifying
    @Query("""
            UPDATE ChatMember cm SET cm.role = 'admin'
            WHERE cm.groupChat.id = :group_chat_id AND cm.user.id = :user_id
            """)
    int promoteUser(@Param("group_chat_id") UUID groupChatId, @Param("user_id") UUID userId);

    @Modifying
    @Query("""
            UPDATE ChatMember cm SET cm.role = 'member'
            WHERE cm.groupChat.id = :group_chat_id AND cm.user.id = :user_id
            """)
    int demoteUser(@Param("group_chat_id") UUID groupChatId, @Param("user_id") UUID userId);
}
