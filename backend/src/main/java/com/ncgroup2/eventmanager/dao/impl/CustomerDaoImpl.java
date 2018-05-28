package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Page;
import com.ncgroup2.eventmanager.entity.Relationship;
import com.ncgroup2.eventmanager.mapper.CustomerMapper;
import com.ncgroup2.eventmanager.mapper.RelationshipMapper;
import com.ncgroup2.eventmanager.util.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import java.util.UUID;

@Repository
@Transactional
@PropertySource("classpath:queries/profile.properties")
public class CustomerDaoImpl extends JdbcDaoSupport implements CustomerDao {

    private static final String BASE_SQL = "SELECT * FROM \"Customer\" ";
    @Autowired
    DataSource dataSource;
    @Value("${getCustomerByLogin}")
    private String getCustomerByLogin;
    @Value("${editCustomer}")
    private String editCustomer;
    @Value("${addFriend}")
    private String addFriend;
    @Value("${checkAddFriend}")
    private String checkAddFriend;
    @Value("${acceptFriend}")
    private String acceptFriend;
    @Value("${rejectFriend}")
    private String rejectFriend;
    @Value("${notifications}")
    private String getNotifications;
    @Value("${countNotifications}")
    private String countNotifications;
    @Value("${uploadAvatar}")
    private String uploadAvatar;
    @Value("${deleteFriend}")
    private String deleteFriend;
    @Value("${getFriends}")
    private String getFriends;
    @Value("${countCustomerByLogin}")
    private String countCustomerByLogin;
    @Value("${searchCustomerByLogin}")
    private String searchCustomerByLogin;
    @Value("${countCustomerByNameAndLastname}")
    private String countCustomerByNameAndLastname;
    @Value("${searchCustomerByNameAndLastname}")
    private String searchCustomerByNameAndLastname;
    @Value("${countCustomerEmptyField}")
    private String countCustomerEmptyField;
    @Value("${searchCustomerEmptyField}")
    private String searchCustomerEmptyField;

    @Value("${create}")
    private String create;
    @Value("${update}")
    private String update;
    @Value("${delete}")
    private String delete;
    @Value("$isExist")
    private String isExist;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void create(Customer customer) {
        Object[] params = customer.getParams();

        params[params.length - 1] = new Timestamp(Instant.now().toEpochMilli());

        this.getJdbcTemplate().update(create, params);
    }

    @Override
    public Customer getById(Object id) {

        String sql = BASE_SQL + "WHERE id = CAST (? AS uuid) ";

        Object[] params = new Object[]{id};

        return this.getJdbcTemplate().queryForObject(sql, params, new CustomerMapper());
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

        return this.getJdbcTemplate().query(sql, params, new CustomerMapper());
    }

    @Override
    public Collection<Customer> getAll() {
        return this.getJdbcTemplate().query(BASE_SQL, new CustomerMapper());
    }

    @Override
    public void updateField(Object id, String fieldName, Object fieldValue) {
        String sql = "UPDATE \"Customer\" SET " + fieldName + " = ? WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{fieldValue, id};

        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public void update(Customer customer) {

        Object[] params = new Object[]{
                customer.getName(),
                customer.getSecondName(),
                customer.getPhone(),
                customer.getLogin(),
                customer.getEmail(),
                customer.getPassword(),
                customer.isVerified(),
                customer.getToken(),
                customer.getAvatar(),
                customer.getId()
        };

        this.getJdbcTemplate().update(update, params);
    }

    @Transactional
    public void delete(Object id) {

        Object[] params = new Object[]{id};

        this.getJdbcTemplate().update(delete, params);
    }

    @Override
    public void deleteUnverifiedCustomers() {

        String sqlUnverified = "DELETE FROM \"Customer\"" +
                "WHERE isVerified = \'false\'" +
                "AND (LOCALTIMESTAMP - registration_date) >= \'24 hour\';";

        this.getJdbcTemplate().update(sqlUnverified);
    }

    @Override
    public Customer getByLogin(String login) {
        Object[] params = new Object[]{login};

        return this.getJdbcTemplate().query(getCustomerByLogin, params, new CustomerMapper()).get(0);
    }

    @Override
    public void edit(Customer customer) {
        Object[] params = new Object[]{customer.getName(), customer.getSecondName(), customer.getPhone(), customer.getAvatar(),
                SecurityContextHolder.getContext().getAuthentication().getName()};

        this.getJdbcTemplate().update(editCustomer, params);
    }

    @Override
    public Page<Customer> search(int pageNo, int pageSize, String search) {
        String newSearch = search.trim();
        String[] subStr = newSearch.toLowerCase().split(" ");

        if (subStr.length > 0) {
            if (subStr.length == 1) {
                return new PaginationHelper<Customer>().getPage(
                        this.getJdbcTemplate(),
                        countCustomerByLogin,
                        searchCustomerByLogin,
                        new Object[]{
                                subStr[0] + "%",
                                SecurityContextHolder.getContext().getAuthentication().getName()},
                        pageNo,
                        pageSize,
                        new CustomerMapper());
            } else {
                return null;
            }
        } else {
            return new PaginationHelper<Customer>().getPage(
                    this.getJdbcTemplate(),
                    countCustomerEmptyField,
                    searchCustomerEmptyField,
                    new Object[]{SecurityContextHolder.getContext().getAuthentication().getName()},
                    pageNo,
                    pageSize,
                    new CustomerMapper());
        }
    }

    @Override
    public List<Customer> getFriends(String login) {
        Object[] params = new Object[] {login, login};

        return this.getJdbcTemplate().query(getFriends, params, new CustomerMapper());
    }

    @Override
    public void delete(String login) {
        Object[] params1 = new Object[] {SecurityContextHolder.getContext().getAuthentication().getName(), login};
        Object[] params2 = new Object[] {login, SecurityContextHolder.getContext().getAuthentication().getName()};

        this.getJdbcTemplate().update(deleteFriend, params1);
        this.getJdbcTemplate().update(deleteFriend, params2);
    }

    @Override
    public synchronized void addFriend(String login) {
        try {
            if (!checkAddFriend(login)) {
                Object[] params = new Object[] {SecurityContextHolder.getContext().getAuthentication().getName(), login};

                this.getJdbcTemplate().update(addFriend, params);
            }
        } catch (Exception e) {
            System.out.println("Unique columns");
        }
    }

    @Override
    public void acceptFriend(String token) {
        Object[] params = new Object[] {UUID.fromString(token)};

        this.getJdbcTemplate().update(acceptFriend, params);
    }

    @Override
    public void rejectFriend(String token) {
        Object[] params = new Object[]{UUID.fromString(token)};

        this.getJdbcTemplate().update(rejectFriend, params);
    }

    @Override
    public List<Relationship> getNotifications(String login) {
        Object[] params = new Object[]{login, login};

        return this.getJdbcTemplate().query(getNotifications, params, new RelationshipMapper());
    }

    @Override
    public void uploadAvatar(Customer customer) {
        Object[] params = new Object[]{"data:image/png;base64," + customer.getAvatar(),
                SecurityContextHolder.getContext().getAuthentication().getName()};

        this.getJdbcTemplate().update(uploadAvatar, params);
    }

    private boolean checkAddFriend(String login) {

        boolean isExist = false;

        Object[] params = new Object[] {SecurityContextHolder.getContext().getAuthentication().getName(), login};

        try (PreparedStatement ps = dataSource.getConnection().prepareStatement(checkAddFriend)) {

            ps.setString(1, (String) params[0]);
            ps.setString(2, (String) params[1]);
            ps.setString(3, (String) params[0]);
            ps.setString(4, (String) params[1]);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isExist = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isExist;
    }

    @Override
    public boolean isCustomerExist(String login) {
        Long countOfCustomers;
        
        Object[] params = new Object[]{
                login
        };
        try {
            countOfCustomers = this.getJdbcTemplate().queryForObject(isExist, params, Long.class);
        } catch (NullPointerException e) {
            return false;
        }
        return countOfCustomers > 0;
    }

    @Override
    public void addGoogleId(String email, String id) {
        String sql = "UPDATE \"Customer\" SET google_id = ? WHERE email = ?";
        Object[] params = new Object[]{
                id, email
        };
        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public boolean isFriends(String currentCustomerId, String customerId) {
        String sql = "SELECT \"isFriends\"(cast(? as uuid), cast(? as uuid))";
        Object[] params = new Object[] {
                currentCustomerId,
                customerId
        };
        return this.getJdbcTemplate().query(sql,params,(resultSet, i) -> resultSet.getBoolean("isFriends")).get(0);
    }

}