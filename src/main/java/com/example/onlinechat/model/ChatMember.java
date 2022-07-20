package com.example.onlinechat.model;

import com.example.onlinechat.model.keys.UserGroupChatPrimaryKey;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "chat_member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatMember {
    public enum Role {
        member, admin
    }

    @Builder.Default
    @EmbeddedId
    UserGroupChatPrimaryKey id = new UserGroupChatPrimaryKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupChatId")
    @JoinColumn(name = "group_chat_id")
    GroupChat groupChat;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatMember that = (ChatMember) o;
        return user != null && groupChat != null
                && Objects.equals(user.getId(), ((ChatMember) o).getUser().getId())
                && Objects.equals(groupChat.getId(), ((ChatMember) o).getGroupChat().getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
