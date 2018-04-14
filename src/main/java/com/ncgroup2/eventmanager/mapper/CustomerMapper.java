package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.beans.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    public static final String BASE_SQL = "SELECT ID, LOGIN, PASSWORD, TRUE FROM \"Customer\" ";
    @Override
    public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSet.getLong("id"));
        customer.setLogin(resultSet.getString("login"));
        customer.setPassword(resultSet.getString("password"));
        return customer;
    }
}
