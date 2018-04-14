package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.beans.Customer;
import com.ncgroup2.eventmanager.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;

@Repository
@Transactional
public class CustomerDAO extends JdbcDaoSupport{

@Autowired
    CustomerDAO (DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public Customer getByLogin(String login) {
        String sql = CustomerMapper.BASE_SQL+ "where login = ?";
        Object[] params = new Object[] {login};
        CustomerMapper mapper = new CustomerMapper();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    public void insert(Customer customer) {
        String sql = "insert into \"Customer\" (id,login,password) values(?,?,?)";
        Object[] params = new Object[] { customer.getId(), customer.getLogin(), customer.getPassword()};
        this.getJdbcTemplate().update(sql, params);

        String sqlRole = "insert into \"Customer_Role\" (customer_id,role_id) values (\n" +
                "                (select id from \"Customer\" where login = ? ),\n" +
                "                (select id from \"Role\" where name = 'USER'))";
        Object[] roleParams = new Object[] {customer.getLogin()};
        this.getJdbcTemplate().update(sqlRole,roleParams);
    }
}
