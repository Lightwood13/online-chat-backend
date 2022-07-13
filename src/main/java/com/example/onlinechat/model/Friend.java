package com.example.onlinechat.model;

import com.example.onlinechat.model.keys.FriendPrimaryKey;

import javax.persistence.*;

@Entity
@Table(name = "friend")
public class Friend {
    @EmbeddedId
    FriendPrimaryKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("friendId")
    @JoinColumn(name = "friend_id")
    User friend;
}
