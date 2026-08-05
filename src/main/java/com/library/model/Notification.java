package com.library.model;

import java.time.LocalDateTime;

public class Notification {
    private int notificationId;
    private int memberId;
    private String message;
    private String type;
    private LocalDateTime createdAt;
    private boolean isRead;

    public Notification() {}

    public Notification(int notificationId, int memberId, String message, String type, LocalDateTime createdAt, boolean isRead) {
        this.notificationId = notificationId;
        this.memberId = memberId;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
