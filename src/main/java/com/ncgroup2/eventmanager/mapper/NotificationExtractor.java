package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Notification;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationExtractor implements ResultSetExtractor<Notification> {

    @Override
    public Notification extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Notification notification = new Notification();

        notification.setName(resultSet.getString("name"));
        notification.setSecond_name(resultSet.getString("second_name"));
        notification.setId(resultSet.getString("id"));

        return notification;
    }
}
