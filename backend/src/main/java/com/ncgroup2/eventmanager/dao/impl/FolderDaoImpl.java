package com.ncgroup2.eventmanager.dao.impl;


import com.ncgroup2.eventmanager.dao.FolderDao;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Folder;
import com.ncgroup2.eventmanager.mapper.FolderMapper;
import com.ncgroup2.eventmanager.util.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public class FolderDaoImpl extends JdbcDaoSupport implements FolderDao {

    public static final String BASE_SQL = "SELECT * FROM \"Folder\" ";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private QueryService queryService;

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
    public void update(Folder entity) {
        String sql = "UPDATE \"Folder\" SET " +
                "name = ?, " +
                "customer_id = ? " +
                " WHERE id = CAST (? AS uuid)";

        Object[] params = entity.getParams();
        this.getJdbcTemplate().update(sql, params);
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
    public void create(Folder entity) {
        String query = queryService.getQuery("folder.create");
        Object[] params = entity.getParams();
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public List<Folder> getAllByCustId(String custId) {
        String query = queryService.getQuery("folder.getAllByCustId");
        Object[] params = new Object[]{
                custId
        };
        return this.getJdbcTemplate().query(query, params,  new BeanPropertyRowMapper(Folder.class));
    }

    @Override
    public List<Event> getNotesByCustIdByFolderId(String custId, String folderId){
        String query = queryService.getQuery("folder.getNotesByCustId");
        Object[] params = new Object[]{
                custId,
                folderId
        };
        return this.getJdbcTemplate().query(query, params,  new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public void moveNoteByNoteIdByFolderName(String noteId, String folderName){
        String query = queryService.getQuery("note.moveByIdByFolderName");
        Object[] params = new Object[]{
                folderName,
                noteId
        };
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public void moveNoteByNoteIdByFolderDefault(String noteId){
        String query = queryService.getQuery("note.moveByIdByFolderDefault");
        Object[] params = new Object[]{
                noteId
        };
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public int getCountByName(String folderName){
        String query = queryService.getQuery("folder.getCountByName");
        Object[] params = new Object[]{
                folderName
        };
        return this.getJdbcTemplate().queryForObject(query, params, int.class);
    }

    @Override
    public void deleteById(String folderId){
        String query = queryService.getQuery("folder.deleteById");
        Object[] params = new Object[]{
                folderId
        };
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public void moveNotesToDefaultFromFolderId(String folderId){
        String query = queryService.getQuery("note.moveByFolderIdToFolderDefault");
        Object[] params = new Object[]{
                folderId
        };
        this.getJdbcTemplate().update(query, params);
    }
}
