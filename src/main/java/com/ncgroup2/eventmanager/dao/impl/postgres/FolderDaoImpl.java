package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.FolderDao;
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
public class FolderDaoImpl extends JdbcDaoSupport  implements FolderDao {

    public static final String BASE_SQL = "SELECT * FROM \"Folder\" ";

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void addFolder(Folder folder) {
        String sql = "INSERT INTO \"Folder\" " +
                "(id, customer_id, name)" +
                " values(uuid_generate_v1(),?,?)";

        Object[] params = folder.getParams();

        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public void deleteFolder(Folder folder) {

        String query = "DELETE FROM Folder WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{
                folder.getId()
        };

        this.getJdbcTemplate().update(query,params);
    }

    @Override
    public Folder getByField(String fieldName, String fieldValue) {

        Collection<Folder> customers = getFolders(fieldName, fieldValue);

        if (!customers.isEmpty()) {

            return getFolders(fieldName, fieldValue).iterator().next();

        } else {

            return null;
        }

    }

    @Override
    public Collection<Folder> getFolders(String fieldName, String fieldValue) {


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
    public Collection<Folder> getFolders() {

        FolderMapper mapper = new FolderMapper();

        try {
            return this.getJdbcTemplate().query(BASE_SQL, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateFolder(Folder folder) {


        String sql = "UPDATE \"Folder\" SET " +
                "name = ?, " +
                "customer_id = ?, " +
                " WHERE id = CAST (? AS uuid)";

        Object[] params = folder.getParams();

        this.getJdbcTemplate().update(sql, params);

    }

    @Override
    public void updateField(Folder folder, String fieldName, Object fieldValue) {

        String sql = "UPDATE \"Folder\" SET " + fieldName + " = ? WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{
                fieldValue,
                folder.getId()
        };

        this.getJdbcTemplate().update(sql, params);
    }
}
