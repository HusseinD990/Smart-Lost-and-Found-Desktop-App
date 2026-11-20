package com.lostandfound.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

public class SqlUtil {

    private SqlUtil() {}

    static Integer getNullableInt(ResultSet rs, String column) throws SQLException
    {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    static void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException
    {
        if (value == null)
        {
            ps.setNull(index, Types.INTEGER);
        }
        else
        {
            ps.setInt(index, value);
        }
    }

    static LocalDateTime toLocalDateTime(Timestamp ts)
    {
        return ts == null ? null : ts.toLocalDateTime();
    }
}