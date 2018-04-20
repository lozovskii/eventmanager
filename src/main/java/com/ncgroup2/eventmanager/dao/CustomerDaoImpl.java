package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.mapper.CustomerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

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
                        "login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "'");
    }

    @Override
    public List<Customer> search(String search) {

        if (search != null && search.trim().length() > 0) {

            String[] subStr = search.toLowerCase().split(" ");

            if (subStr.length == 1) {
                String query = "SELECT * FROM \"Customer\" WHERE LOWER(name) LIKE '"
                        +subStr[0]+"%' OR LOWER(second_name) LIKE '"+subStr[0]+"%'";

                return new JdbcTemplate(dataSource).query(query, new CustomerRowMapper());
            } else if (subStr.length == 2) {
                String query1 = "SELECT * FROM \"Customer\" WHERE LOWER(name) LIKE '"
                        +subStr[0]+"%' AND LOWER(second_name) LIKE '"+subStr[1]+"%'";
                String query2 = "SELECT * FROM \"Customer\" WHERE LOWER(second_name) LIKE '"
                        +subStr[0]+"%' AND LOWER(name) LIKE '"+subStr[1]+"%'";
                String result = query1 + " UNION " + query2;

                return new JdbcTemplate(dataSource).query(result, new CustomerRowMapper());
            } else {
                return null;
            }
        } else if (search == "" || search == null) {
            String query = "SELECT * FROM \"Customer\"";

            return new JdbcTemplate(dataSource).query(query, new CustomerRowMapper());
        } else {
            return null;
        }
    }

    @Override
    public List<Customer> getFriends(String login) {
        String query = "SELECT * FROM \"Customer\" WHERE id IN " +
                "(SELECT recipient_id FROM \"Relationship\" R INNER JOIN \"Customer\" C ON R.sender_id = C.id " +
                "WHERE C.login = '" + login + "' AND R.status = 3)";

        return new JdbcTemplate(dataSource).query(query, new CustomerRowMapper());
    }
}