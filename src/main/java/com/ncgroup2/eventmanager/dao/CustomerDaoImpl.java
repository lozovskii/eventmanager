package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.mapper.CustomerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
@Transactional
public class CustomerDaoImpl implements CustomerDao {

    @Autowired
    DataSource dataSource;

    @Override
    public Customer getByLogin(String login) {
        return new JdbcTemplate(dataSource).query(
                "SELECT * FROM \"Customer\" WHERE login = '" + login + "'", new CustomerRowMapper()).get(0);
    }

    @Override
    public void edit(Customer customer) {
        new JdbcTemplate(dataSource).update(
                "UPDATE \"Customer\" SET name = '" + customer.getFirstName() + "', " +
                        "second_name = '" + customer.getLastName() +"', " +
                        "phone = '" + customer.getPhone() + "' WHERE " +
                        "login = '" + customer.getLogin() + "'");
    }
}