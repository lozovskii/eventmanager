package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getString("id"));
        item.setName(resultSet.getString("name"));
        item.setDescription(resultSet.getString("description"));
        item.setImage(resultSet.getBytes("image"));
        item.setLink(resultSet.getString("link"));
        return null;
    }
}