package com.lostandfound.dao;

import com.lostandfound.model.Claim;
import com.lostandfound.model.ClaimStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClaimDAO {

    private static final String SELECT_ALL =
        "SELECT claim_id, found_report_id, claimant_id, ownership_details, status, reviewer_id, "
      + "decision_reason, decision_date, created_at FROM claims";

    // CREATE
    public void insert(Claim claim)
    {
        String sql = "INSERT INTO claims (found_report_id, claimant_id, ownership_details, status) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, claim.getFoundReportId());
            ps.setInt(2, claim.getClaimantId());
            ps.setString(3, claim.getOwnershipDetails());
            ps.setString(4, claim.getStatus().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys())
            {
                if (keys.next())
                {
                    claim.setClaimId(keys.getInt(1));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not insert claim", e);
        }
    }

    // READ
    public Optional<Claim> findById(int claimId)
    {
        List<Claim> result = query(SELECT_ALL + " WHERE claim_id = ?", List.of(claimId));
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public List<Claim> findByClaimant(int claimantId)
    {
        return query(SELECT_ALL + " WHERE claimant_id = ? ORDER BY created_at DESC", List.of(claimantId));
    }

    public List<Claim> findByFoundReport(int foundReportId)
    {
        return query(SELECT_ALL + " WHERE found_report_id = ? ORDER BY created_at DESC", List.of(foundReportId));
    }

    public List<Claim> findByStatus(ClaimStatus status)
    {
        return query(SELECT_ALL + " WHERE status = ? ORDER BY created_at", List.of(status.name()));
    }

    public List<Claim> findAll()
    {
        return query(SELECT_ALL + " ORDER BY created_at DESC", List.of());
    }

    /** Used to stop the same user from claiming the same item twice. */
    public boolean existsActiveClaim(int claimantId, int foundReportId)
    {
        String sql = "SELECT COUNT(*) FROM claims WHERE claimant_id = ? AND found_report_id = ? "
                   + "AND status IN ('PENDING', 'UNDER_REVIEW', 'APPROVED')";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, claimantId);
            ps.setInt(2, foundReportId);
            try (ResultSet rs = ps.executeQuery())
            {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not check existing claims", e);
        }
    }

    // UPDATE
    public void updateStatus(int claimId, ClaimStatus status)
    {
        String sql = "UPDATE claims SET status = ? WHERE claim_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, status.name());
            ps.setInt(2, claimId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not update claim status", e);
        }
    }

    /** Records who decided, why, and when. decision_date is set by the database. */
    public void recordDecision(int claimId, ClaimStatus status, int reviewerId, String reason)
    {
        String sql = "UPDATE claims SET status = ?, reviewer_id = ?, decision_reason = ?, "
                   + "decision_date = CURRENT_TIMESTAMP WHERE claim_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1, status.name());
            ps.setInt(2, reviewerId);
            ps.setString(3, reason);
            ps.setInt(4, claimId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not record claim decision", e);
        }
    }

    // DELETE (withdrawing a claim)
    public void delete(int claimId)
    {
        String sql = "DELETE FROM claims WHERE claim_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, claimId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Could not delete claim", e);
        }
    }

    // helpers
    private List<Claim> query(String sql, List<?> params)
    {
        List<Claim> list = new ArrayList<>();
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
            throw new DataAccessException("Could not query claims", e);
        }
        return list;
    }

    private Claim mapRow(ResultSet rs) throws SQLException
    {
        Claim c = new Claim();
        c.setClaimId(rs.getInt("claim_id"));
        c.setFoundReportId(rs.getInt("found_report_id"));
        c.setClaimantId(rs.getInt("claimant_id"));
        c.setOwnershipDetails(rs.getString("ownership_details"));
        c.setStatus(ClaimStatus.valueOf(rs.getString("status")));
        c.setReviewerId(SqlUtil.getNullableInt(rs, "reviewer_id"));
        c.setDecisionReason(rs.getString("decision_reason"));
        c.setDecisionDate(SqlUtil.toLocalDateTime(rs.getTimestamp("decision_date")));
        c.setCreatedAt(SqlUtil.toLocalDateTime(rs.getTimestamp("created_at")));
        return c;
    }
}