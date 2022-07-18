package com.example.onlinechat.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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

    public static User withId(UUID id) {
        final User result = new User();
        result.setId(id);
        return result;
    }
}
