package com.example.onlinechat.repository;

import com.example.onlinechat.model.Friend;
import com.example.onlinechat.model.keys.FriendPrimaryKey;
import com.example.onlinechat.service.projections.UserProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends CrudRepository<Friend, FriendPrimaryKey> {
    @Query(value = "SELECT" +
            " cast(uf.id AS varchar)    AS id," +
            " uf.name                   AS name," +
            " uf.profile_photo_location AS profilePhotoLocation" +
            " FROM users u" +
            " JOIN friend f on u.id = f.user_id" +
            " JOIN users uf on uf.id = f.friend_id" +
            " WHERE u.username = :username",
            nativeQuery = true)
    List<UserProjection> getFriendsByUserUsername(@Param("username") String username);
}
