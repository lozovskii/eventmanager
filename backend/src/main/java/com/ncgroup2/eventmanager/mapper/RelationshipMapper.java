package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Relationship;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RelationshipMapper implements RowMapper<Relationship> {

    @Override
    public Relationship mapRow(ResultSet resultSet, int i) throws SQLException {
        Relationship relationship = new Relationship();

        relationship.setId(resultSet.getString("id"));
        relationship.setName(resultSet.getString("name"));
        relationship.setSecond_name(resultSet.getString("second_name"));
        relationship.setLogin(resultSet.getString("login"));

        return relationship;
    }
}
