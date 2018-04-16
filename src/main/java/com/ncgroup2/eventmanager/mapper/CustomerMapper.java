package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;


public class CustomerMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSet.getLong("id"));
        customer.setLogin(resultSet.getString("login"));
        customer.setPassword(resultSet.getString("password"));
        customer.setEmail(resultSet.getString("email"));
        customer.setName(resultSet.getString("name"));
        customer.setSecondName(resultSet.getString("second_name"));
        customer.setVerified(resultSet.getBoolean("isVerified"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setRegistrationDate(Instant.ofEpochMilli(resultSet.getTimestamp("registration_date").getTime()));
        return customer;
    }
}
