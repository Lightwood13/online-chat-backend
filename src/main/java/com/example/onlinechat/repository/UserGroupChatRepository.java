package com.example.onlinechat.repository;

import com.example.onlinechat.model.UserGroupChat;
import com.example.onlinechat.model.keys.UserGroupChatPrimaryKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserGroupChatRepository extends CrudRepository<UserGroupChat, UserGroupChatPrimaryKey> {
    boolean existsByUserUsernameAndGroupChatId(String username, UUID groupChatId);

    @Query(value = "SELECT DISTINCT cast(ugc.user_id AS varchar)" +
            " FROM user_group_chat ugc" +
            " WHERE ugc.group_chat_id IN" +
            "    (SELECT ugc2.group_chat_id" +
            "    FROM user_group_chat ugc2" +
            "    WHERE cast(ugc2.user_id AS varchar) = :userId)",
            nativeQuery = true)
    List<String> getUserIdsThatShareGroupChatWith(@Param("userId") String userId);
}
