package com.example.onlinechat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "group_chat")
@Getter
@Setter
public class GroupChat {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String profilePhotoLocation;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_group_chat",
            joinColumns = @JoinColumn(name = "group_chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> members;

    public static GroupChat of(UUID id) {
        final GroupChat result = new GroupChat();
        result.setId(id);
        return result;
    }
}
