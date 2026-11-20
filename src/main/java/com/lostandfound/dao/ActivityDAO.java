package com.lostandfound.dao;

import com.lostandfound.model.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {

    private static final String SELECT_ALL =
        "SELECT activity_id, user_id, action, details, report_id, claim_id, created_at FROM activity_history";

    // CREATE
    public void insert(Activity a)
    {
        String sql = "INSERT INTO activity_history (user_id, action, details, report_id, claim_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getAction());
            ps.setString(3, a.getDetails());
            SqlUtil.setNullableInt(ps, 4, a.getReportId());
            SqlUtil.setNullableInt(ps, 5, a.getClaimId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next())
                {
                    a.setActivityId(keys.getInt(1));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not insert activity", e);
        }
    }

    // READ
    /** Most recent first; used for the recent-activity timeline. */
    public List<Activity> findRecentByUser(int userId, int limit)
    {
        return query(SELECT_ALL + " WHERE user_id = ? ORDER BY created_at DESC, activity_id DESC LIMIT ?",
                     List.of(userId, limit));
    }

    public List<Activity> findByReport(int reportId)
    {
        return query(SELECT_ALL + " WHERE report_id = ? ORDER BY created_at DESC, activity_id DESC", List.of(reportId));
    }

    public List<Activity> findByClaim(int claimId)
    {
        return query(SELECT_ALL + " WHERE claim_id = ? ORDER BY created_at DESC, activity_id DESC", List.of(claimId));
    }

    // helpers
    private List<Activity> query(String sql, List<?> params)
    {
        List<Activity> list = new ArrayList<>();
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
            throw new DataAccessException("Could not query activity", e);
        }
        return list;
    }

    private Activity mapRow(ResultSet rs) throws SQLException
    {
        Activity a = new Activity();
        a.setActivityId(rs.getInt("activity_id"));
        a.setUserId(rs.getInt("user_id"));
        a.setAction(rs.getString("action"));
        a.setDetails(rs.getString("details"));
        a.setReportId(SqlUtil.getNullableInt(rs, "report_id"));
        a.setClaimId(SqlUtil.getNullableInt(rs, "claim_id"));
        a.setCreatedAt(SqlUtil.toLocalDateTime(rs.getTimestamp("created_at")));
        return a;
    }
}