package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class CustomerMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
        Customer customer = new Customer();

        customer.setId(resultSet.getString("id"));
        customer.setName(resultSet.getString("name"));
        customer.setSecondName(resultSet.getString("second_name"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setLogin(resultSet.getString("login"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPassword(resultSet.getString("password"));
        customer.setVerified(resultSet.getBoolean("isverified"));
        customer.setToken(resultSet.getString("token"));
        customer.setAvatar(resultSet.getString("avatar"));
        customer.setRegistrationDate(Instant.ofEpochMilli(
                resultSet.getTimestamp("registration_date").getTime()));

        return customer;
    }
}