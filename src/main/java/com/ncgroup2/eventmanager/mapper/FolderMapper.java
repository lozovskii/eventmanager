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
        folder.setCustomer_id(resultSet.getString("customer_id"));
        folder.setTitle(resultSet.getString("title"));

        return folder;
    }
}