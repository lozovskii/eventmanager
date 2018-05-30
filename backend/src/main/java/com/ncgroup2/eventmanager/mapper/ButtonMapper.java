package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Friends;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ButtonMapper implements RowMapper<Friends> {
    @Override
    public Friends mapRow(ResultSet resultSet, int i) throws SQLException {
        Friends friend = new Friends();

        friend.setCurrent(resultSet.getString("current"));
        friend.setAnother(resultSet.getString("another"));
        friend.setIsfriends(resultSet.getBoolean("isfriends"));
        friend.setIsrequest(resultSet.getBoolean("isrequest"));

        return friend;
    }
}
