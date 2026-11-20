package com.lostandfound.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/config.properties"))
        {
            if (in == null)
            {
                throw new IllegalStateException(
                    "config.properties not found. Copy config.example.properties to config.properties and set your password.");
            }
            props.load(in);
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Could not read config.properties", e);
        }
    }

    private DatabaseConnection() {}

    public static Connection getConnection()
    {
        try
        {
            return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not connect to the database", e);
        }
    }
}