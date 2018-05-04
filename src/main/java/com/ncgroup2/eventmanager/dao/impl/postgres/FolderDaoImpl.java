package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.DAO;
import com.ncgroup2.eventmanager.entity.Entity;
import com.ncgroup2.eventmanager.entity.Folder;
import com.ncgroup2.eventmanager.mapper.FolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Collection;

@Repository
@Transactional
public class FolderDaoImpl extends JdbcDaoSupport  implements DAO {

    public static final String BASE_SQL = "SELECT * FROM \"Folder\" ";

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    @Override
    public Collection<Folder> getAll() {

        FolderMapper mapper = new FolderMapper();

        try {

            return this.getJdbcTemplate().query(BASE_SQL, mapper);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public Folder getEntityByField(String fieldName, Object fieldValue) {

        String value = String.valueOf(fieldValue);

        Collection<Folder> customers = getEntitiesByField(fieldName, value);

        if (!customers.isEmpty()) {

            return customers.iterator().next();

        } else {

            return null;
        }
    }

    @Override
    public Collection<Folder> getEntitiesByField(String fieldName, Object fieldValue) {

        String sql = BASE_SQL + "WHERE " + fieldName + " = ?";

        Object[] params = new Object[]{fieldValue};

        FolderMapper mapper = new FolderMapper();

        try {
            return this.getJdbcTemplate().query(sql, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Folder getById(Object id) {

        String sql = BASE_SQL + "WHERE id = CAST (" + id + " AS uuid) ";

        Object[] params = new Object[]{id};

        FolderMapper mapper = new FolderMapper();

        try {

            return this.getJdbcTemplate().queryForObject(sql, params, mapper);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Entity entity) {

        String sql = "UPDATE \"Folder\" SET " +
                "name = ?, " +
                "customer_id = ? " +
                " WHERE id = CAST (? AS uuid)";

        Object[] params = entity.getParams();

        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public void updateField(Object id, String fieldName, Object fieldValue) {

        String folderId = String.valueOf(id);

        String sql = "UPDATE \"Folder\" SET " + fieldName + " = ? WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{
                fieldValue,
                folderId
        };

        this.getJdbcTemplate().update(sql, params);
    }


    @Override
    public void delete(Object id) {

        String folderId = String.valueOf(id);

        String query = "DELETE FROM Folder WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{ folderId };

        this.getJdbcTemplate().update(query,params);
    }

    @Override
    public void create(Entity entity) {

        String sql = "INSERT INTO \"Folder\" " +
                "(id, customer_id, name)" +
                " values(uuid_generate_v1(),?,?)";

        Object[] params = entity.getParams();

        this.getJdbcTemplate().update(sql, params);

    }

}
