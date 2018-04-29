package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.DAO;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Collection;

public class ItemDaoImpl extends JdbcDaoSupport implements DAO<Item,Object> {

    public static final String BASE_SQL = "SELECT * FROM \"Item\" ";

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    @Override
    public Collection<Item> getAll() {

        ItemMapper mapper = new ItemMapper();

        try {

            return this.getJdbcTemplate().query(BASE_SQL, mapper);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Item getById(Object id) {

        String sql = BASE_SQL + "WHERE id = CAST (" + id + " AS uuid) ";

        Object[] params = new Object[]{id};

        ItemMapper mapper = new ItemMapper();

        try {

            return this.getJdbcTemplate().queryForObject(sql, params, mapper);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Item getEntityByField(String fieldName, Object fieldValue) {
        Collection<Item> items = getEntitiesByField(fieldName, fieldValue);
        if (!items.isEmpty()) {
            return items.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public Collection<Item> getEntitiesByField(String fieldName, Object fieldValue) {

        String sql = BASE_SQL + "WHERE " + fieldName + " = ?";

        Object[] params = new Object[]{fieldValue};
        ItemMapper mapper = new ItemMapper();
        try {
            return this.getJdbcTemplate().query(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public void update(Item item) {

        String sql = "UPDATE \"Item\" SET " +
                "name = ?, " +
                "description = ?, " +
                "image = ?, " +
                "link = ?, " +
                " WHERE id = CAST (? AS UUID)";

        Object[] params = item.getParams();

        this.getJdbcTemplate().update(sql, params);
    }


    @Override
    public void updateField(Object id, String fieldName, Object fieldValue) {

        String sql = "UPDATE \"Item\" SET " + fieldName + " = ? WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{
                fieldValue,
                id
        };

        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public void delete(Object id) {

        String query = "DELETE FROM Item WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{ id };

        this.getJdbcTemplate().update(query,params);

    }

    @Override
    public void create(Item item) {

        String query = "INSERT INTO \"Item\" " +
                "(id,name,description, image ,link)" +
                "VALUES(uuid_generate_v1(),?,?,?,?)";

//        Object[] params = item.getParams();
        Object[] params = new Object[]{
                item.getName(),
                item.getDescription(),
                item.getImage(),
                item.getLink()
        };

        this.getJdbcTemplate().update(query,params);
    }

}
