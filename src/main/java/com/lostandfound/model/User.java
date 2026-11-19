package com.lostandfound.model;

import java.time.LocalDateTime;

public class User {
    private int           userId;
    private Role          role;
    private String        username;
    private String        email;
    private String        passwordHash;
    private String        fullName;
    private LocalDateTime createdAt;

    public User() {}

    public User(String username, String email, String passwordHash, String fullName, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
    }

    public boolean isAdmin()
    {
        return role == Role.ADMIN;
    }

    public void validate()
    {
        if (username == null || username.trim().length() < 3)
        {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
        if (email == null || !email.contains("@"))
        {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (fullName == null || fullName.isBlank())
        {
            throw new IllegalArgumentException("Full name is required");
        }
        if (role == null)
        {
            throw new IllegalArgumentException("Role is required");
        }
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPasswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
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