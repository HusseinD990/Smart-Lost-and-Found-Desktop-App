package com.lostandfound.model;

import java.time.LocalDateTime;

public class Notification {
    private int notificationId;
    private int userId;
    private NotificationType type;
    private String message;
    private Integer relatedId;
    private boolean read;
    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(int userId, NotificationType type, String message, Integer relatedId) {
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.relatedId = relatedId;
        this.read = false;
    }

    public int getNotificationId()
    {
        return notificationId;
    }

    public void setNotificationId(int notificationId)
    {
        this.notificationId = notificationId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public NotificationType getType()
    {
        return type;
    }

    public void setType(NotificationType type)
    {
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Integer getRelatedId()
    {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId)
    {
        this.relatedId = relatedId;
    }

    public boolean isRead()
    {
        return read;
    }

    public void setRead(boolean read)
    {
        this.read = read;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}