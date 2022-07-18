package com.example.onlinechat.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "group_chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupChat {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String profilePhotoLocation;

    private Timestamp createdOn;

    @OneToMany(mappedBy = "groupChat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<ChatMember> members;

    public static GroupChat withId(UUID id) {
        final GroupChat result = new GroupChat();
        result.setId(id);
        return result;
    }
}
