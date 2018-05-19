package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.LocationDao;
import com.ncgroup2.eventmanager.entity.Location;
import com.ncgroup2.eventmanager.mapper.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.Collection;

public class LocationDaoImpl extends JdbcDaoSupport implements LocationDao {

    private static final String BASE_SQL = "SELECT * FROM \"Location\" ";

    @Autowired
    DataSource dataSource;

    @Override
    public Collection<Location> getAll() {
        return this.getJdbcTemplate().query(BASE_SQL, new LocationMapper());
    }

    @Override
    public Location getById(Object id) {
        String getSqlLoc = BASE_SQL + "WHERE id = CAST (" + id + " AS uuid)";

        Object[] params = new Object[] {id};
        return this.getJdbcTemplate().queryForObject(getSqlLoc, params, new LocationMapper());
    }

    @Override
    public Location getEntityByField(String fieldName, Object fieldValue) {
        return null;
    }

    @Override
    public Collection<Location> getEntitiesByField(String fieldName, Object fieldValue) {
        return null;
    }

    @Override
    public void update(Location entity) {

    }

    @Override
    public void updateField(Object id, String fieldName, Object fieldValue) {

    }

    @Override
    public void delete(Object id) {

    }

    @Override
    public void create(Location location) {
        String insertSqlLoc =
                "INSERT INTO \"Location\"" +
        "(id, event_id, country, city, street, house) " +
                "VALUES (uuid_generate_v1(),?,?,?,?,?)";

        this.getJdbcTemplate().update(insertSqlLoc, location.getParams());
    }
}
