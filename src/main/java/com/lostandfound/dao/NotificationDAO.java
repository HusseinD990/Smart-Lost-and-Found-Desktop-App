package com.lostandfound.dao;

import com.lostandfound.model.Notification;
import com.lostandfound.model.NotificationType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    // CREATE
    public void insert(Notification n)
    {
        String sql = "INSERT INTO notifications (user_id, type, message, related_id) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, n.getUserId());
            ps.setString(2, n.getType().name());
            ps.setString(3, n.getMessage());
            SqlUtil.setNullableInt(ps, 4, n.getRelatedId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next())
                {
                    n.setNotificationId(keys.getInt(1));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not insert notification", e);
        }
    }

    // READ
    public List<Notification> findByUser(int userId)
    {
        String sql = "SELECT notification_id, user_id, type, message, related_id, is_read, created_at "
                   + "FROM notifications WHERE user_id = ? ORDER BY created_at DESC, notification_id DESC";
        List<Notification> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    list.add(mapRow(rs));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not load notifications", e);
        }
        return list;
    }

    public int countUnread(int userId)
    {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = FALSE";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                rs.next();
                return rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not count notifications", e);
        }
    }

    // UPDATE
    public void markAsRead(int notificationId)
    {
        executeUpdate("UPDATE notifications SET is_read = TRUE WHERE notification_id = ?", notificationId);
    }

    public void markAllAsRead(int userId)
    {
        executeUpdate("UPDATE notifications SET is_read = TRUE WHERE user_id = ?", userId);
    }

    // DELETE
    public void delete(int notificationId)
    {
        executeUpdate("DELETE FROM notifications WHERE notification_id = ?", notificationId);
    }

    // helpers
    private void executeUpdate(String sql, int id)
    {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not update notifications", e);
        }
    }

    private Notification mapRow(ResultSet rs) throws SQLException
    {
        Notification n = new Notification();
        n.setNotificationId(rs.getInt("notification_id"));
        n.setUserId(rs.getInt("user_id"));
        n.setType(NotificationType.valueOf(rs.getString("type")));
        n.setMessage(rs.getString("message"));
        n.setRelatedId(SqlUtil.getNullableInt(rs, "related_id"));
        n.setRead(rs.getBoolean("is_read"));          // DB column is_read -> Java field read
        n.setCreatedAt(SqlUtil.toLocalDateTime(rs.getTimestamp("created_at")));
        return n;
    }
}