package com.library.dao.impl;

import com.library.config.DatabaseConfig;
import com.library.dao.NotificationDAO;
import com.library.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImpl implements NotificationDAO {

    @Override
    public void createNotification(Notification notification) {
        String sql = "INSERT INTO notifications (member_id, message, type, is_read) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, notification.getMemberId());
            statement.setString(2, notification.getMessage());
            statement.setString(3, notification.getType());
            statement.setBoolean(4, notification.isRead());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Notification> getNotificationsForMember(int memberId) {
        String sql = "SELECT * FROM notifications WHERE member_id=? ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                notifications.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notifications;
    }

    @Override
    public List<Notification> getAllNotifications() {
        String sql = "SELECT * FROM notifications ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                notifications.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notifications;
    }

    @Override
    public void markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read=true WHERE notification_id=?";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, notificationId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Notification mapRow(ResultSet rs) throws SQLException {
        return new Notification(
                rs.getInt("notification_id"),
                rs.getInt("member_id"),
                rs.getString("message"),
                rs.getString("type"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getBoolean("is_read")
        );
    }
}
