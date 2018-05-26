package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.LocationDao;
import com.ncgroup2.eventmanager.entity.Location;
import com.ncgroup2.eventmanager.mapper.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Collection;

@Repository
@Transactional
public class LocationDaoImpl extends JdbcDaoSupport implements LocationDao {

    private static final String BASE_SQL = "SELECT * FROM \"Location\" ";

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public Collection<Location> getAll() {
        return this.getJdbcTemplate().query(BASE_SQL, new LocationMapper());
    }

    @Override
    public Location getById(Object id) {
        String getSqlLoc = BASE_SQL + "WHERE id = CAST (? AS UUID)";

        Object[] params = new Object[] {id};
        return this.getJdbcTemplate().queryForObject(getSqlLoc, params, new LocationMapper());
    }

    @Override
    public Location getByEventId(String event_id) {
        Location location;
        String getSqlLoc = BASE_SQL + "WHERE event_id = CAST (? AS UUID)";
        Object[] params = new Object[]{event_id};
        try {
            location = this.getJdbcTemplate().queryForObject(getSqlLoc, params, new LocationMapper());
        }catch(EmptyResultDataAccessException e){
            location = null;
        }
        return location;
    }

    @Override
    public Location getEntityByField(String fieldName, Object fieldValue) {
        Collection<Location> locations = getEntitiesByField(fieldName, fieldValue);
        if (!locations.isEmpty()) {
            return getEntitiesByField(fieldName,fieldValue).iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public Collection<Location> getEntitiesByField(String fieldName, Object fieldValue) {
        String getSqlFildLoc = BASE_SQL + "WHERE " + fieldName + " = ?";
        Object[] params = new Object[]{fieldValue};
        return this.getJdbcTemplate().query(getSqlFildLoc, params, new LocationMapper());
    }

    @Override
    public void update(Location location) {
        String sql = "UPDATE \"Location\" SET " +
                "country = ?, " +
                "city = ?, " +
                "street = ?, " +
                "house = ?, "+
                "latitude = ?, "+
                "longitude = ? "+
                "WHERE event_id = CAST (? AS UUID)";

        Object[] params = new Object[] {
                location.getCountry(),
                location.getCity(),
                location.getStreet(),
                location.getHouse(),
                location.getLatitude(),
                location.getLongitude(),
                location.getEvent_id()
        };

        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public void updateField(Object id, String fieldName, Object fieldValue) {
        String sql = "UPDATE \"Location\" SET " + fieldName + " = ? WHERE id = CAST (? AS uuid)";

        Object[] params = new  Object[] {fieldValue, id};

        this.getJdbcTemplate().update(sql, params);

    }

    @Override
    public void delete(Object id) {
        String sql = "DELETE EROM \"Location\" WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[] {id};

        this.getJdbcTemplate().update(sql, params);

    }

    @Override
    public void create(Location location) {
        String insertSqlLoc =
                "INSERT INTO \"Location\"" +
                        "(id, event_id, country, city, street, house, latitude, longitude) " +
                        "VALUES (CAST(? AS UUID),CAST(? AS UUID),?,?,?,?,?,?)";

        this.getJdbcTemplate().update(insertSqlLoc, location.getParams());
    }



}
