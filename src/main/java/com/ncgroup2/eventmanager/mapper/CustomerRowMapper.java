package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
        return new CustomerExtractor().extractData(resultSet);
    }
}