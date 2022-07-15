package com.example.onlinechat.repository;

import com.example.onlinechat.model.Friend;
import com.example.onlinechat.model.User;
import com.example.onlinechat.model.keys.FriendPrimaryKey;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendRepository extends CrudRepository<Friend, FriendPrimaryKey> {
    @Query("SELECT uf" +
            " FROM Friend f JOIN f.friend uf" +
            " WHERE f.user.id = :user_id AND f.pending = false")
    List<User> getFriendsByUserId(@Param("user_id") UUID userId);

    @Query("SELECT u" +
            " FROM Friend f JOIN f.user u" +
            " WHERE f.friend.id = :user_id AND f.pending = true")
    List<User> getPendingFriendRequestsByUserId(@Param("user_id") UUID userId);

    @Modifying
    @Query("DELETE FROM Friend f" +
            " WHERE (f.user.id = :user_id    AND f.friend.id = :friend_id AND f.pending = false)" +
            " OR    (f.user.id = :friend_id  AND f.friend.id = :user_id   AND f.pending = false)")
    int removeFriend(@Param("user_id") UUID userId, @Param("friend_id") UUID friendId);
}
