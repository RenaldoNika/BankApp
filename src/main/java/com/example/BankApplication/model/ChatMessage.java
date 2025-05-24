package com.example.BankApplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sender;

    private String recipient; // përdoruesi që merr mesazhin


    private String content;

    private LocalDateTime timestamp;

    @PrePersist
    public void setTimestamp() {
        this.timestamp = LocalDateTime.now();
    }

    @Transient
    public String getFormattedTimestamp() {
        if (timestamp == null) return "";
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }
}
