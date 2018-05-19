package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet resultSet, int i) throws SQLException {
        Message message = new Message();

        message.setId(resultSet.getString("id"));
        message.setChatId(resultSet.getString("chat_id"));
        message.setAuthorId(resultSet.getString("author_id"));
        message.setContent(resultSet.getString("content"));
        message.setDate(LocalDateTime.parse(resultSet.getString("date")));

        return message;
    }
}
