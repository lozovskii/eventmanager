package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Entity;
import com.ncgroup2.eventmanager.entity.Relationship;
import com.ncgroup2.eventmanager.mapper.CustomerMapper;
import com.ncgroup2.eventmanager.mapper.RelationshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public class CustomerDaoImpl extends JdbcDaoSupport implements CustomerDao {

    public static final String BASE_SQL = "SELECT * FROM \"Customer\" ";

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void create(Customer customer) {

        String sql = "INSERT INTO \"Customer\" " +
                "(id,name,second_name,phone,login,email,password,isverified,token,avatar,registration_date)" +
                " values(uuid_generate_v1(),?,?,?,?,?,?,?,?,?,?)";

        Object[] params = customer.getParams();

        params[params.length-1] = new Timestamp(Instant.now().toEpochMilli());

        this.getJdbcTemplate().update(sql, params);

        String sqlRole = "INSERT INTO \"Customer_Role\" VALUES (" +
                "uuid_generate_v1()," +
                "(SELECT id FROM \"Customer\" WHERE login = ?)," +
                "(SELECT id FROM \"Role\" WHERE name = 'USER')" +
                ")";

        Object[] roleParams = new Object[]{customer.getLogin()};
        this.getJdbcTemplate().update(sqlRole, roleParams);

    }

    @Override
    public Customer getById(Object id) {
        String sql = BASE_SQL + "WHERE id = CAST (" + id + " AS uuid) ";

        Object[] params = new Object[]{id};

        CustomerMapper mapper = new CustomerMapper();

        try {
            return this.getJdbcTemplate().queryForObject(sql, params, mapper);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Customer getEntityByField(String fieldName, Object fieldValue) {
        Collection<Customer> customers = getEntitiesByField(fieldName, fieldValue);
        if (!customers.isEmpty()) {
            return getEntitiesByField(fieldName, fieldValue).iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public Collection<Customer> getEntitiesByField(String fieldName, Object fieldValue) {

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
    public Collection<Customer> getAll() {

        CustomerMapper mapper = new CustomerMapper();

        try {
            return this.getJdbcTemplate().query(BASE_SQL, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateField(Object id, String fieldName, Object fieldValue) {

        String sql = "UPDATE \"Customer\" SET " + fieldName + " = ? WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{
                fieldValue,
                id
        };

        this.getJdbcTemplate().update(sql, params);

    }

    @Override
    public void update(Customer customer) {

        String sql = "UPDATE \"Customer\" SET " +
                "name = ?, " +
                "second_name = ?, " +
                "phone = ?, " +
                "login = ?, " +
                "email = ?, " +
                "password = ?, " +
                "isverified = ?, " +
                "token = ?, " +
                "avatar = ? " +
                " WHERE id = CAST (? AS UUID)";

        Object[] params = customer.getParams();

        this.getJdbcTemplate().update(sql, params);
    }

    @Transactional
    public void delete(Object id) {

        String sqlCustomer = "DELETE FROM \"Customer\" WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{id};

        this.getJdbcTemplate().update(sqlCustomer, params);

    }

    @Override
    public void deleteUnverifiedCustomers() {

        String sqlUnverified = "DELETE FROM \"Customer\"" +
                "WHERE isVerified = \'false\'" +
                "AND (LOCALTIMESTAMP - registration_date) >= \'24 hour\';";

        this.getJdbcTemplate().update(sqlUnverified);
    }


    // PROFILE METHODS IMPLEMENTATION

    @Override
    public Customer getByLogin(String login) {
        return this.getJdbcTemplate().query(
                "SELECT * FROM \"Customer\" WHERE login = '" + login + "'", new CustomerMapper()).get(0);
    }

    @Override
    public void edit(Customer customer) {
        String query = "UPDATE \"Customer\" SET name = '" + customer.getName() + "', " +
                "second_name = '" + customer.getSecondName() +"', " +
                "phone = '" + customer.getPhone() + "' WHERE " +
                "login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "'";

        this.getJdbcTemplate().update(query);
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

                return this.getJdbcTemplate().query(result, new CustomerMapper());
            } else {
                return null;
            }
        } else if (search.equals("")) {
            String query = "SELECT * FROM \"Customer\" WHERE login != '" +
                    SecurityContextHolder.getContext().getAuthentication().getName() + "'";

            return this.getJdbcTemplate().query(query, new CustomerMapper());
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

        return this.getJdbcTemplate().query(query, new CustomerMapper());
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

        this.getJdbcTemplate().update(query1);
        this.getJdbcTemplate().update(query2);
    }

    @Override
    public void addFriend(String login) {
        if (!checkAddFriend(login)) {
            String query = "INSERT INTO \"Relationship\" (sender_friend_id, recipient_friend_id, status) VALUES (" +
                    "(SELECT id FROM \"Customer\" WHERE login = '" +
                    SecurityContextHolder.getContext().getAuthentication().getName() + "')," +
                    "(SELECT id FROM \"Customer\" WHERE login = '" + login + "'),1)";

            this.getJdbcTemplate().update(query);
        }
    }

    @Override
    public void acceptFriend(String token) {
        String query = "UPDATE \"Relationship\" SET status = 3 WHERE id = '" + token + "' " +
                "AND recipient_friend_id IN (SELECT id FROM \"Customer\" WHERE login = '"
                + SecurityContextHolder.getContext().getAuthentication().getName() + "')";

        this.getJdbcTemplate().update(query);
    }

    @Override
    public void rejectFriend(String token) {
        String query = "UPDATE \"Relationship\" SET status = 2 WHERE id = '" + token + "'";

        this.getJdbcTemplate().update(query);
    }

    @Override
    public List<Relationship> getNotifications(String login) {
        String query =
                "SELECT c.name, c.second_name, r.id FROM \"Customer\" c, \"Relationship\" r " +
                "WHERE r.recipient_friend_id IN (SELECT id FROM \"Customer\" WHERE login = '" + login + "' ) " +
                "AND status = 1 " +
                "AND c.login!='" + login + "'";

        return this.getJdbcTemplate().query(query, new RelationshipMapper());
    }

    @Override
    public void uploadAvatar(Customer customer) {
        String query = "UPDATE \"Customer\" SET avatar = 'data:image/png;base64," + customer.getAvatar() + "' WHERE " +
                "login = '" + SecurityContextHolder.getContext().getAuthentication().getName() + "'";

        this.getJdbcTemplate().update(query);
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