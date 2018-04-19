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

    private final static String getByLogin = "SELECT * FROM \"Customer\" WHERE login = '";
    private final static String update = "UPDATE \"Customer\" SET name = ?, second_name = ?, phone = ? WHERE login = '";

    @Autowired
    DataSource dataSource;

    @Override
    public Customer getByLogin(String login) {
        return new JdbcTemplate(dataSource).query(getByLogin + login + "'", new CustomerRowMapper()).get(0);
    }

    @Override
    public void edit(Customer customer) {
        new JdbcTemplate(dataSource).update(update, new Object[]
                {customer.getFirstName(),customer.getLastName(),customer.getPhone()});
    }
}