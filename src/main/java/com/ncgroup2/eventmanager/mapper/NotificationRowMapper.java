package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Notification;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationRowMapper implements RowMapper<Notification> {

    @Override
    public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
        return new NotificationExtractor().extractData(resultSet);
    }
}
