package com.example.onlinechat.service.dto;

import java.sql.Timestamp;

public interface GroupChatWithLastMessageDTO {
    String getId();

    String getName();

    String getProfilePhotoLocation();

    String getLastMessageId();

    String getLastMessageAuthorId();

    String getLastMessageText();

    Timestamp getLastMessageSentOn();
}
