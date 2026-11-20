package com.lostandfound.dao;

import com.lostandfound.model.Role;
import com.lostandfound.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    private static final String SELECT_ALL =
        "SELECT user_id, username, email, password_hash, full_name, role, created_at FROM users";

    public void insert(User user)
    {
        String sql = "INSERT INTO users (username, email, password_hash, full_name, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getRole().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next())
                {
                    user.setUserId(keys.getInt(1));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not insert user", e);
        }
    }

    public Optional<User> findById(int userId)
    {
        return findOne(SELECT_ALL + " WHERE user_id = ?", userId);
    }

    public Optional<User> findByUsername(String username)
    {
        return findOne(SELECT_ALL + " WHERE username = ?", username);
    }

    public Optional<User> findByEmail(String email)
    {
        return findOne(SELECT_ALL + " WHERE email = ?", email);
    }

    public List<User> findAll()
    {
        List<User> users = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL + " ORDER BY user_id");
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                users.add(mapRow(rs));
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not load users", e);
        }
        return users;
    }

    public void update(User user)
    {
        String sql = "UPDATE users SET username = ?, email = ?, full_name = ?, role = ? WHERE user_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole().name());
            ps.setInt(5, user.getUserId());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not update user", e);
        }
    }

    public void updatePasswordHash(int userId, String newHash)
    {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not update password", e);
        }
    }

    public void delete(int userId)
    {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not delete user", e);
        }
    }

    private Optional<User> findOne(String sql, Object param)
    {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setObject(1, param);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not query users", e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException
    {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setFullName(rs.getString("full_name"));
        u.setRole(Role.valueOf(rs.getString("role")));
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return u;
    }
}