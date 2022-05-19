package com.example.onlinechat;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String messageText;

    protected Message() {
    }

    public Message(String sender, String messageText) {
        this.sender = sender;
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return String.format("Message[id=%d, sender='%s', messageText='%s']",
                id, sender, messageText);
    }

    public Long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageText() {
        return messageText;
    }

}
