package com.ncgroup2.eventmanager.mapper;

import com.ncgroup2.eventmanager.entity.Customer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerExtractor implements ResultSetExtractor<Customer> {

    @Override
    public Customer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Customer customer = new Customer();

        customer.setFirstName(resultSet.getString("name"));
        customer.setLastName(resultSet.getString("second_name"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setLogin(resultSet.getString("login"));
        customer.setEmail(resultSet.getString("email"));

        return customer;
    }
}