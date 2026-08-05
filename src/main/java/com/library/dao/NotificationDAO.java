package com.library.dao;

import com.library.model.Notification;

import java.util.List;

public interface NotificationDAO {
    void createNotification(Notification notification);
    List<Notification> getNotificationsForMember(int memberId);
    List<Notification> getAllNotifications();
    void markAsRead(int notificationId);
}
