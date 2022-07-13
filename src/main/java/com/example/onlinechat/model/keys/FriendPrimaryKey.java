package com.example.onlinechat.model.keys;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class FriendPrimaryKey implements Serializable {
    UUID userId;
    UUID friendId;
}
