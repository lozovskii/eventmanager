package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Relationship;
import com.ncgroup2.eventmanager.mapper.CustomerMapper;
import com.ncgroup2.eventmanager.mapper.RelationshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class CustomerDaoImpl implements CustomerDao {

    @Autowired
    DataSource dataSource;

    @Override
    public Customer getByLogin(String login) {
        return new JdbcTemplate(dataSource).query(
                "SELECT * FROM \"Customer\" WHERE login = '" + login + "'", new CustomerMapper()).get(0);
    }

    @Override
    public void edit(Customer customer) {
        String query = "UPDATE \"Customer\" SET name = '" + customer.getName() + "', " +
                "second_name = '" + customer.getSecondName() +"', " +
                "phone = '" + customer.getPhone() + "' WHERE " +
                "login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "'";

        new JdbcTemplate(dataSource).update(query);
    }

    @Override
    public List<Customer> search(String search) {

        if (search != null && search.trim().length() > 0) {

            String[] subStr = search.toLowerCase().split(" ");

            if (subStr.length == 2) {
                String query1 = "SELECT * FROM \"Customer\" WHERE LOWER(name) LIKE '"
                        +subStr[0]+"%' AND LOWER(second_name) LIKE '"+subStr[1]+"%' AND login != '" +
                        SecurityContextHolder.getContext().getAuthentication().getName() + "'";
                String query2 = "SELECT * FROM \"Customer\" WHERE LOWER(second_name) LIKE '"
                        +subStr[0]+"%' AND LOWER(name) LIKE '"+subStr[1]+"%' AND login != '" +
                        SecurityContextHolder.getContext().getAuthentication().getName() + "'";
                String result = query1 + " UNION " + query2;

                return new JdbcTemplate(dataSource).query(result, new CustomerMapper());
            } else {
                return null;
            }
        } else if (search.equals("")) {
            String query = "SELECT * FROM \"Customer\" WHERE login != '" +
                    SecurityContextHolder.getContext().getAuthentication().getName() + "'";

            return new JdbcTemplate(dataSource).query(query, new CustomerMapper());
        } else {
            return null;
        }
    }

    @Override
    public List<Customer> getFriends(String login) {
        String query = "SELECT * FROM \"Customer\" WHERE id IN (" +
                "SELECT recipient_friend_id FROM \"Relationship\" R INNER JOIN \"Customer\" C " +
                "ON R.sender_friend_id = C.id WHERE C.login = '" + login + "' AND R.status = 3 UNION " +
                "SELECT sender_friend_id FROM \"Relationship\" R INNER JOIN \"Customer\" C " +
                "ON R.recipient_friend_id = C.id WHERE C.login = '" + login + "' AND R.status = 3);";

        return new JdbcTemplate(dataSource).query(query, new CustomerMapper());
    }

    @Override
    public void delete(String login) {
        String query1 = "DELETE FROM \"Relationship\" " +
                "WHERE sender_friend_id IN (SELECT id FROM \"Customer\" " +
                "WHERE login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "') " +
                "AND recipient_friend_id IN (SELECT id FROM \"Customer\" WHERE login = '" + login + "')";

        String query2 = "DELETE FROM \"Relationship\" " +
                "WHERE sender_friend_id IN (SELECT id FROM \"Customer\" WHERE login = '" + login + "') " +
                "AND recipient_friend_id IN (SELECT id FROM \"Customer\" " +
                "WHERE login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "')";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query1);
        jdbcTemplate.update(query2);
    }

    @Override
    public void addFriend(String login) {
        if (!checkAddFriend(login)) {
            String query = "INSERT INTO \"Relationship\" (sender_friend_id, recipient_friend_id, status) VALUES (" +
                    "(SELECT id FROM \"Customer\" WHERE login = '" +
                    SecurityContextHolder.getContext().getAuthentication().getName() + "')," +
                    "(SELECT id FROM \"Customer\" WHERE login = '" + login + "'),1)";

                    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            jdbcTemplate.update(query);
        }
    }

    @Override
    public void acceptFriend(String token) {
        String query = "UPDATE \"Relationship\" SET status = 3 WHERE id = '" + token + "' " +
                "AND recipient_friend_id IN (SELECT id FROM \"Customer\" WHERE login = '"
                + SecurityContextHolder.getContext().getAuthentication().getName() + "')";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query);
    }

    @Override
    public void rejectFriend(String token) {
        String query = "UPDATE \"Relationship\" SET status = 2 WHERE id = '" + token + "'";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query);
    }

    @Override
    public List<Relationship> getNotifications(String login) {
        String query =
                "SELECT c.name, c.second_name, r.id FROM \"Customer\" c, \"Relationship\" r " +
                "WHERE r.recipient_friend_id IN (SELECT id FROM \"Customer\" WHERE login = '" + login + "' ) " +
                "AND status = 1 " +
                "AND c.login!='" + login + "'";

        return new JdbcTemplate(dataSource).query(query, new RelationshipMapper());
    }

    private boolean checkAddFriend(String login) {
        boolean isExist = false;

        try {
            String query = "SELECT * FROM \"Relationship\" WHERE sender_friend_id IN " +
                    "(SELECT id FROM \"Customer\" WHERE login = '"
                    + SecurityContextHolder.getContext().getAuthentication().getName() + "') AND recipient_friend_id IN " +
                    "(SELECT id FROM \"Customer\" WHERE login = '" + login + "') AND status = 3";

            String query1 = "SELECT * FROM \"Relationship\" WHERE recipient_friend_id IN " +
                    "(SELECT id FROM \"Customer\" WHERE login = '"
                    + SecurityContextHolder.getContext().getAuthentication().getName() + "') AND sender_friend_id IN " +
                    "(SELECT id FROM \"Customer\" WHERE login = '" + login + "') AND status = 3";

            PreparedStatement ps = dataSource.getConnection().prepareStatement(query);
            PreparedStatement ps1 = dataSource.getConnection().prepareStatement(query1);
            ResultSet rs = ps.executeQuery();
            ResultSet rs1 = ps1.executeQuery();

            if (rs.next()) {
                isExist = true;
            }

            if (rs1.next()) {
                isExist = true;
            }

            rs.close();
            rs1.close();
            ps.close();
            ps1.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isExist;
    }
}