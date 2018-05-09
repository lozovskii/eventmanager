package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Event;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Event event = new Event();
        event.setId(resultSet.getString("id"));
        event.setGroupId(resultSet.getString("groupId"));
        event.setFolderId(resultSet.getString("folderId"));
        event.setCreatorId(resultSet.getString("creatorId"));
        event.setStartTime(LocalDateTime.parse(resultSet.getString("startTime")));
        event.setEndTime(LocalDateTime.parse(resultSet.getString("endTime")));
        event.setName(resultSet.getString("name"));
        event.setVisibility(resultSet.getString("visibility"));
        event.setStatus(resultSet.getString("status"));
        event.setDescription(resultSet.getString("description"));
        return event;
    }
}
