package com.example.onlinechat.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String username;

    private String name;

    private String encryptedPassword;

    private String profilePhotoLocation;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    Set<GroupChat> groupChats;

    public static User of(UUID id) {
        final User result = new User();
        result.setId(id);
        return result;
    }
}
