package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Location;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationMapper implements RowMapper<Location> {
    @Override
    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        Location location = new Location();

        location.setId(rs.getString("id"));
        location.setEvent_id(rs.getString("event_id"));
        location.setCountry(rs.getString("country"));
        location.setCity(rs.getString("city"));
        location.setStreet(rs.getString("street"));
        location.setHouse(rs.getString("house"));
        location.setLatitude(rs.getDouble("latitude"));
        location.setLongitude((rs.getDouble("longitude")));
        return location;
    }
}


