package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Rating_Item;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Rating_ItemMapper implements RowMapper<Rating_Item> {

    @Override
    public Rating_Item mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Rating_Item rating = new Rating_Item();
        rating.setId(resultSet.getString("id"));
        rating.setCustomer_login("customer_login");
        rating.setItem_id("item_id");
        return rating;
    }
}
