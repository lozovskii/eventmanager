package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Event;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Event event = new Event();
        event.setId(resultSet.getString("id"));
        event.setId(resultSet.getString("folderId"));
        event.setId(resultSet.getString("creatorId"));
        event.setStartTime(Instant.ofEpochMilli(resultSet.getTimestamp("startTime").getTime()));
        event.setEndTime(Instant.ofEpochMilli(resultSet.getTimestamp("endTime").getTime()));
        event.setPriority(resultSet.getString("priority"));
        event.setName(resultSet.getString("name"));
        event.setPublic(resultSet.getBoolean("isPublic"));
        event.setFrequency(resultSet.getString("frequency"));
        event.setStatus(resultSet.getString("status"));

        return event;
    }
}
