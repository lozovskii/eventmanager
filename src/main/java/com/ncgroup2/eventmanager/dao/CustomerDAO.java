package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.beans.Customer;
import com.ncgroup2.eventmanager.mappers.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;

@Repository
@Transactional
public class CustomerDAO extends JdbcDaoSupport {
    public static final String BASE_SQL = "SELECT * FROM \"Customer\" ";

    @Autowired
    CustomerDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

//    public Customer getByLogin(String login) {
//        String sql = CustomerMapper.BASE_SQL+ "where login = ?";
//        Object[] params = new Object[] {login};
//        CustomerMapper mapper = new CustomerMapper();
//        try {
//            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }

    @Transactional
    public Customer insert(Customer customer) {
        String sql = "insert into \"Customer\" (id,login,password,email,name,second_name,phone,isverified,registration_date) values(?,?,?,?,?,?,?,?,?)";
        Object[] params = new Object[]{
                customer.getId(),
                customer.getLogin(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getName(),
                customer.getSecondName(),
                customer.getPhone(),
                customer.isVerified(),
                new Timestamp(Instant.now().toEpochMilli())};
        this.getJdbcTemplate().update(sql, params);

        String sqlRole = "insert into \"Customer_Role\" (customer_id,role_id) values (\n" +
                "                (select id from \"Customer\" where login = ? ),\n" +
                "                (select id from \"Role\" where name = 'USER'))";
        Object[] roleParams = new Object[]{customer.getLogin()};
        this.getJdbcTemplate().update(sqlRole, roleParams);

        return getByField("login", customer.getLogin());
    }

    public void updateField(Customer customer, String fieldName, Object fieldValue) {
        String sql = "update \"Customer\" set " + fieldName + " = ? where id = ?";
        Object[] params = new Object[]{
                fieldValue,
                customer.getId()
        };
        this.getJdbcTemplate().update(sql, params);
    }

    public Customer getByField(String fieldName, String fieldValue) {
        String sql = BASE_SQL + "where " + fieldName + " = ?";
        Object[] params = new Object[]{fieldValue};
        CustomerMapper mapper = new CustomerMapper();
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    public void delete(Customer customer) {
        String sqlCustomerRole = "delete from \"Customer_Role\" where customer_id = ?";
        String sqlCustomer = "delete from \"Customer\" where id = ?";
        Object[] params = new Object[]{customer.getId()};
        this.getJdbcTemplate().update(sqlCustomerRole, params);
        this.getJdbcTemplate().update(sqlCustomer, params);
    }
}
