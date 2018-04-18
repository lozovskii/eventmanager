package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

@Repository
@Transactional
public class CustomerDaoImpl extends JdbcDaoSupport implements CustomerDao {

    public static final String BASE_SQL = "SELECT * FROM \"Customer\" ";

    //    @Autowired
//    CustomerDaoImpl(DataSource dataSource) {
//        this.setDataSource(dataSource);
//    }
    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void updateField(Customer customer, String fieldName, Object fieldValue) {

        String sql = "UPDATE \"Customer\" SET " + fieldName + " = ? WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{
                fieldValue,
                customer.getId()
        };

        this.getJdbcTemplate().update(sql, params);

    }

    @Override
    public Customer getByField(String fieldName, String fieldValue) {
        Collection<Customer> customers = getCustomers(fieldName, fieldValue);
        if (customers != null) {
            return getCustomers(fieldName, fieldValue).iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public void addCustomer(Customer customer) {

        // Add uuid_generate_v1()

        String sql = "INSERT INTO \"Customer\" " +
                "(id,name,second_name,phone,login,email,password,isverified,registration_date)" +
                " values(uuid_generate_v1(),?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{
                customer.getName(),
                customer.getSecondName(),
                customer.getPhone(),
                customer.getLogin(),
                customer.getEmail(),
                customer.getPassword(),
                customer.isVerified(),
                new Timestamp(Instant.now().toEpochMilli())};
        this.getJdbcTemplate().update(sql, params);

        String sqlRole = "INSERT INTO \"Customer_Role\" VALUES (" +
                "uuid_generate_v1()," +
                "(SELECT id FROM \"Customer\" WHERE login = ?)," +
                "(SELECT id FROM \"Role\" WHERE name = 'USER')" +
                ")";

        Object[] roleParams = new Object[]{customer.getLogin()};
        this.getJdbcTemplate().update(sqlRole, roleParams);

        //return getByField("login", customer.getLogin());

    }

    @Transactional
    @Override
    public void deleteCustomer(Customer customer) {

        //String sqlCustomerRole = "DELETE FROM \"Customer_Role\" WHERE customer_id = ?";
        //FK записи в БД мають параметр ON DELETE CASCADE

        String sqlCustomer = "DELETE FROM \"Customer\" WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{customer.getId()};

        //this.getJdbcTemplate().update(sqlCustomerRole, params);
        this.getJdbcTemplate().update(sqlCustomer, params);

    }

    @Override
    public Collection<Customer> getCustomers(String fieldName, String fieldValue) {

        String sql = BASE_SQL + "WHERE " + fieldName + " = ?";

        Object[] params = new Object[]{fieldValue};
        CustomerMapper mapper = new CustomerMapper();

        try {
            return this.getJdbcTemplate().query(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Collection<Customer> getCustomers() {

        CustomerMapper mapper = new CustomerMapper();

        try {
            return this.getJdbcTemplate().query(BASE_SQL, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE \"Customer\" SET " +
                "name = ? " +
                "second_name = ? " +
                "password = ? " +
                "phone = ? " +
                "isverified = ? " +
                "token = ? " +
                "image = ? " +
                " WHERE id = CAST (? AS uuid)";
        Object[] params = new Object[]{
                customer.getName(),
                customer.getSecondName(),
                customer.getPassword(),
                customer.getPhone(),
                customer.isVerified(),
                customer.getToken(),
                customer.getImage()};

        this.getJdbcTemplate().update(sql, params);
    }
}
