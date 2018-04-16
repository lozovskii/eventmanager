package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

@Repository
@Transactional
public class CustomerDaoImpl extends JdbcDaoSupport implements CustomerDao{

    public static final String BASE_SQL = "SELECT * FROM \"Customer\" ";

    @Autowired
    CustomerDaoImpl(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Override
    public void updateField(Customer customer, String fieldName, Object fieldValue) {

        String sql = "UPDATE \"Customer\" SET " + fieldName + " = ? WHERE id = ?";

        Object[] params = new Object[]{
                fieldValue,
                customer.getId()
        };

        this.getJdbcTemplate().update(sql, params);

    }


    public Customer getByField(String fieldName, String fieldValue) {

        String sql = BASE_SQL + "WHERE " + fieldName + " = ?";

        Object[] params = new Object[]{fieldValue};
        CustomerMapper mapper = new CustomerMapper();

        try {

            return this.getJdbcTemplate().queryForObject(sql, params, mapper);

        } catch (EmptyResultDataAccessException e) {

            return null;

        }

    }

    @Override
    public void addCustomer(Customer customer) {

        String sql = "INSERT INTO \"Customer\" (login,password,email,name,second_name,phone,isverified,registration_date) values(?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{
                customer.getLogin(),
                customer.getPassword(),
                customer.getEmail(),
                customer.getName(),
                customer.getSecondName(),
                customer.getPhone(),
                customer.isVerified(),
                new Timestamp(Instant.now().toEpochMilli())};
        this.getJdbcTemplate().update(sql, params);

        String sqlRole = "INSERT INTO \"Customer_Role\" (customer_id,role_id) VALUES (\n" +
                "                (SELECT id FROM \"Customer\" WHERE login = ? ),\n" +
                "                (SELECT id FROM \"Role\" WHERE name = 'USER'))";

        Object[] roleParams = new Object[]{customer.getLogin()};
        this.getJdbcTemplate().update(sqlRole, roleParams);

        //return getByField("login", customer.getLogin());

    }

    @Transactional
    @Override
    public void deleteCustomer(Customer customer) {

        //String sqlCustomerRole = "DELETE FROM \"Customer_Role\" WHERE customer_id = ?";
        //FK записи в БД мають параметр ON DELETE CASCADE

        String sqlCustomer = "DELETE FROM \"Customer\" WHERE id = ?";

        Object[] params = new Object[]{customer.getId()};

        //this.getJdbcTemplate().update(sqlCustomerRole, params);
        this.getJdbcTemplate().update(sqlCustomer, params);

    }

    @Override
    public Collection<Customer> getCustomers(String fieldName, String fieldValue) {

        Collection<Customer>  customers = new ArrayList<>();

        customers.add(getByField(fieldName,fieldValue));

        return customers;
    }

    @Override
    public Collection<Customer> getCustomers() {
        return null;
    }


    @Override
    public void updateCustomer(Customer customer) {

    }
}
