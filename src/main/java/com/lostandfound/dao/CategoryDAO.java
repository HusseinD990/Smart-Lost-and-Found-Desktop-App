package com.lostandfound.dao;

import com.lostandfound.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAO {

    public void insert(Category category)
    {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next())
                {
                    category.setCategoryId(keys.getInt(1));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not insert category", e);
        }
    }

    public List<Category> findAll()
    {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, name, description FROM categories ORDER BY name";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery())
        {
            while (rs.next())
            {
                list.add(mapRow(rs));
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not load categories", e);
        }
        return list;
    }

    public Optional<Category> findById(int categoryId)
    {
        String sql = "SELECT category_id, name, description FROM categories WHERE category_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not load category", e);
        }
    }

    public void update(Category category)
    {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not update category", e);
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException
    {
        return new Category(rs.getInt("category_id"), rs.getString("name"), rs.getString("description"));
    }
}