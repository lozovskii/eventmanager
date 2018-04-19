package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<Chat> {

    @Override
    public Chat mapRow(ResultSet resultSet, int i) throws SQLException {
        Chat chat = new Chat();
        chat.setId(resultSet.getString("id"));
        chat.setEventId(resultSet.getString("event_id"));
        chat.setWithOwner(resultSet.getBoolean("withOwner"));
        return chat;
    }
}
