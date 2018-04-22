package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Notification;
import com.ncgroup2.eventmanager.mapper.CustomerRowMapper;
import com.ncgroup2.eventmanager.mapper.NotificationRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String query = "UPDATE \"Customer\" SET name = '" + customer.getFirstName() + "', " +
                "second_name = '" + customer.getLastName() +"', " +
                "phone = '" + customer.getPhone() + "' WHERE " +
                "login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "'";

        new JdbcTemplate(dataSource).update(query);
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
        } else if (search.equals("")) {
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

    @Override
    public void delete(String login) {
        String query1 = "DELETE FROM \"Relationship\" " +
                "WHERE sender_id IN (SELECT id FROM \"Customer\" " +
                "WHERE login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "') " +
                "AND recipient_id IN (SELECT id FROM \"Customer\" WHERE login = '" + login + "')";

        String query2 = "DELETE FROM \"Relationship\" " +
                "WHERE sender_id IN (SELECT id FROM \"Customer\" WHERE login = '" + login + "') " +
                "AND recipient_id IN (SELECT id FROM \"Customer\" " +
                "WHERE login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "')";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query1);
        jdbcTemplate.update(query2);
    }

    @Override
    public void addFriend(String login) {
        String query1 = "INSERT INTO \"Relationship\" (sender_id, recipient_id, status) " +
                "VALUES ((SELECT id FROM \"Customer\" WHERE login = '" +
                SecurityContextHolder.getContext().getAuthentication().getName() + "')," +
                "(SELECT id FROM \"Customer\" WHERE login = '" + login + "'),1)";

        String query2 = "INSERT INTO \"Notification\" (sender_id, recipient_id, status) VALUES (" +
                "(SELECT id FROM \"Customer\" WHERE login = '" +
                SecurityContextHolder.getContext().getAuthentication().getName() + "')," +
                "(SELECT id FROM \"Customer\" WHERE login = '" + login + "')," +
                "(SELECT concat(name,' ',second_name,' wants to add you to your friends list') " +
                "FROM \"Customer\" WHERE login = '" +
                SecurityContextHolder.getContext().getAuthentication().getName() + "'))";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query1);
        jdbcTemplate.update(query2);
    }

    @Override
    public void acceptFriend(String uuid) {
        String query1 = "DELETE FROM \"Notification\" WHERE sender_id = '" + uuid + "'" +
                " AND recipient_id IN (SELECT id FROM \"Customer\" WHERE login = '" +
                SecurityContextHolder.getContext().getAuthentication().getName() + "')";

        String query2 = "UPDATE \"Relationship\" SET status = 3 WHERE sender_id = '" + uuid + "'" +
                " AND recipient_id IN (SELECT id FROM \"Customer\" WHERE login = '" +
                SecurityContextHolder.getContext().getAuthentication().getName() + "');";

        String query3 = "INSERT INTO \"Relationship\" (sender_id, recipient_id, status) VALUES (" +
                "(SELECT id FROM \"Customer\" WHERE login = '" +
                SecurityContextHolder.getContext().getAuthentication().getName() + "'),'" + uuid + "',3)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query1);
        jdbcTemplate.update(query2);
        jdbcTemplate.update(query3);
    }

    @Override
    public void rejectFriend(String uuid) {
        String query1 = "DELETE FROM \"Notification\" WHERE sender_id = '" + uuid + "'" +
                " AND recipient_id IN (SELECT id FROM \"Customer\" WHERE login = '" +
                SecurityContextHolder.getContext().getAuthentication().getName() + "')";

        String query2 = "DELETE FROM \"Relationship\" WHERE sender_id = '" + uuid + "'" +
                " AND recipient_id IN (SELECT id FROM \"Customer\" WHERE login = '" +
                SecurityContextHolder.getContext().getAuthentication().getName() + "')";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query1);
        jdbcTemplate.update(query2);
    }

    @Override
    public Map<Notification, String> getNotifications(String login) {
        String query = "SELECT * FROM \"Notification\" WHERE recipient_id IN " +
                "(SELECT id FROM \"Customer\" WHERE login = '" + login + "')";

        Map<Notification, String> map = new HashMap<>();
        List<Notification> notifications = new JdbcTemplate(dataSource).query(query, new NotificationRowMapper());

        for(Notification notification : notifications) {
            map.put(notification, notification.getSender());
        }

        return map;
    }
}