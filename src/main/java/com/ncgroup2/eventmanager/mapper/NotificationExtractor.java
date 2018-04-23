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

        notification.setStatus(resultSet.getString("status"));
        notification.setSender(resultSet.getString("sender_id"));
        notification.setRecipient(resultSet.getString("recipient_id"));
        notification.setToken(resultSet.getString("token"));
        notification.setRequest(resultSet.getString("request"));

        return notification;
    }
}
