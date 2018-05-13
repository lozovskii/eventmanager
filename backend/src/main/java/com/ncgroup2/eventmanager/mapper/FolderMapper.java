package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Folder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FolderMapper implements RowMapper<Folder> {

    @Override
    public Folder mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Folder folder = new Folder();
        folder.setId(resultSet.getString("id"));
        folder.setName(resultSet.getString("title"));
        folder.setCustomerId(resultSet.getString("customerId"));
        folder.setIsshared(resultSet.getBoolean("isshared"));
        return folder;
    }

}
