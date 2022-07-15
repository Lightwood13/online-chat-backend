package com.example.onlinechat.model.keys;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserGroupChatPrimaryKey implements Serializable {
    @Column(name = "user_id")
    UUID userId;

    @Column(name = "group_chat_id")
    UUID groupChatId;
}
