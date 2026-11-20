package com.lostandfound.dao;

import com.lostandfound.model.FoundReport;
import com.lostandfound.model.ItemReport;
import com.lostandfound.model.LostReport;
import com.lostandfound.model.ReportStatus;
import com.lostandfound.model.ReportType;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemReportDAO {

    private static final String SELECT_BASE =
        "SELECT r.report_id, r.report_type, r.user_id, r.category_id, c.name AS category_name, "
      + "r.title, r.description, r.color, r.brand, r.distinguishing_features, r.event_date, "
      + "r.location, r.image_path, r.status, r.created_at, r.updated_at "
      + "FROM item_reports r JOIN categories c ON r.category_id = c.category_id";

    public void insert(ItemReport report)
    {
        String sql = "INSERT INTO item_reports (report_type, user_id, category_id, title, description, color, "
                   + "brand, distinguishing_features, event_date, location, image_path, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setString(1, report.getReportType().name());
            ps.setInt(2, report.getUserId());
            ps.setInt(3, report.getCategoryId());
            ps.setString(4, report.getTitle());
            ps.setString(5, report.getDescription());
            ps.setString(6, report.getColor());
            ps.setString(7, report.getBrand());
            ps.setString(8, report.getDistinguishingFeatures());
            ps.setDate(9, Date.valueOf(report.getEventDate()));
            ps.setString(10, report.getLocation());
            ps.setString(11, report.getImagePath());
            ps.setString(12, report.getStatus().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next())
                {
                    report.setReportId(keys.getInt(1));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not insert report", e);
        }
    }

    public Optional<ItemReport> findById(int reportId)
    {
        List<ItemReport> result = query(SELECT_BASE + " WHERE r.report_id = ?", List.of(reportId));
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public List<ItemReport> findByUser(int userId)
    {
        return query(SELECT_BASE + " WHERE r.user_id = ? ORDER BY r.created_at DESC", List.of(userId));
    }

    public List<ItemReport> findByType(ReportType type, ReportStatus status)
    {
        return query(SELECT_BASE + " WHERE r.report_type = ? AND r.status = ? ORDER BY r.created_at DESC",
                     List.of(type.name(), status.name()));
    }

    public List<ItemReport> search(String keyword, Integer categoryId, LocalDate from, LocalDate to,
                                   String location, String color, ReportStatus status, ReportType type)
    {
        StringBuilder sql = new StringBuilder(SELECT_BASE).append(" WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank())
        {
            sql.append(" AND (r.title LIKE ? OR r.description LIKE ? OR r.brand LIKE ? OR r.distinguishing_features LIKE ?)");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }
        if (categoryId != null)
        {
            sql.append(" AND r.category_id = ?");
            params.add(categoryId);
        }
        if (from != null)
        {
            sql.append(" AND r.event_date >= ?");
            params.add(Date.valueOf(from));
        }
        if (to != null)
        {
            sql.append(" AND r.event_date <= ?");
            params.add(Date.valueOf(to));
        }
        if (location != null && !location.isBlank())
        {
            sql.append(" AND r.location LIKE ?");
            params.add("%" + location.trim() + "%");
        }
        if (color != null && !color.isBlank())
        {
            sql.append(" AND r.color LIKE ?");
            params.add("%" + color.trim() + "%");
        }
        if (status != null)
        {
            sql.append(" AND r.status = ?");
            params.add(status.name());
        }
        if (type != null)
        {
            sql.append(" AND r.report_type = ?");
            params.add(type.name());
        }
        sql.append(" ORDER BY r.created_at DESC");

        return query(sql.toString(), params);
    }

    public void update(ItemReport report)
    {
        String sql = "UPDATE item_reports SET category_id = ?, title = ?, description = ?, color = ?, brand = ?, "
                   + "distinguishing_features = ?, event_date = ?, location = ?, image_path = ?, status = ? "
                   + "WHERE report_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, report.getCategoryId());
            ps.setString(2, report.getTitle());
            ps.setString(3, report.getDescription());
            ps.setString(4, report.getColor());
            ps.setString(5, report.getBrand());
            ps.setString(6, report.getDistinguishingFeatures());
            ps.setDate(7, Date.valueOf(report.getEventDate()));
            ps.setString(8, report.getLocation());
            ps.setString(9, report.getImagePath());
            ps.setString(10, report.getStatus().name());
            ps.setInt(11, report.getReportId());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not update report", e);
        }
    }

    public void updateStatus(int reportId, ReportStatus status)
    {
        String sql = "UPDATE item_reports SET status = ? WHERE report_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, status.name());
            ps.setInt(2, reportId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not update report status", e);
        }
    }

    public void delete(int reportId)
    {
        String sql = "DELETE FROM item_reports WHERE report_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, reportId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not delete report", e);
        }
    }

    private List<ItemReport> query(String sql, List<?> params)
    {
        List<ItemReport> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            for (int i = 0; i < params.size(); i++)
            {
                ps.setObject(i + 1, params.get(i));
            }
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
            throw new DataAccessException("Could not query reports", e);
        }
        return list;
    }

    private ItemReport mapRow(ResultSet rs) throws SQLException
    {
        ReportType type = ReportType.valueOf(rs.getString("report_type"));
        ItemReport r = (type == ReportType.LOST) ? new LostReport() : new FoundReport();

        r.setReportId(rs.getInt("report_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setCategoryId(rs.getInt("category_id"));
        r.setCategoryName(rs.getString("category_name"));
        r.setTitle(rs.getString("title"));
        r.setDescription(rs.getString("description"));
        r.setColor(rs.getString("color"));
        r.setBrand(rs.getString("brand"));
        r.setDistinguishingFeatures(rs.getString("distinguishing_features"));
        r.setEventDate(rs.getDate("event_date").toLocalDate());
        r.setLocation(rs.getString("location"));
        r.setImagePath(rs.getString("image_path"));
        r.setStatus(ReportStatus.valueOf(rs.getString("status")));

        Timestamp created = rs.getTimestamp("created_at");
        Timestamp updated = rs.getTimestamp("updated_at");
        r.setCreatedAt(created == null ? null : created.toLocalDateTime());
        r.setUpdatedAt(updated == null ? null : updated.toLocalDateTime());
        return r;
    }
}