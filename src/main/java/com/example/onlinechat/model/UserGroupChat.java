package com.example.onlinechat.model;

import com.example.onlinechat.model.keys.UserGroupChatPrimaryKey;

import javax.persistence.*;

@Entity
@Table(name = "user_group_chat")
public class UserGroupChat {
    @EmbeddedId
    UserGroupChatPrimaryKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupChatId")
    @JoinColumn(name = "group_chat_id")
    GroupChat groupChat;
}
