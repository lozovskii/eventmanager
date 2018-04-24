package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.mapper.EventMapper;
import com.ncgroup2.eventmanager.util.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class EventDaoImpl extends JdbcDaoSupport implements EventDao {

    @Autowired
    private QueryService queryService;

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void createEvent(Event event) {

        String query = "INSERT INTO \"Event\" " +
                "(id,name,folder_id, creator_id ,start_time,end_time,priority,visibility,frequency_value,description,status)" +
                "VALUES(uuid_generate_v1(),?,null,null,?,?,?,(SELECT id FROM \"Event_Visibility\" WHERE name = \'PRIVATE\'),?,?," +
                "(SELECT id FROM \"Event_Status\" WHERE name = \'NON - OCCURRED\'));";

//        String query = queryService.getQuery("create.event");

        Object[] params = new Object[]{
                event.getName(),
                event.getStartTime(),
                event.getEndTime(),
                event.getPriority(),
                event.getFrequency(),
                event.getDescription()
        };
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public void deleteEvent(Event event){
        String query = "DELETE FROM Event WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{
                event.getId()
        };

        this.getJdbcTemplate().update(query,params);
    }

    @Override
    public void updateField(Event event, String fieldName, Object fieldValue) {

        String sql = "UPDATE \"Event\" SET " + fieldName + " = ? WHERE id = CAST (? AS uuid)";

        Object[] params = new Object[]{
                fieldValue,
                event.getId()
        };

        this.getJdbcTemplate().update(sql, params);

    }

    @Override
    public int getIdByField(String fieldName, String fieldValue) {

        String sql = "SELECT id FROM \"Event_Status\" WHERE " + fieldName + " = ?";
        Object[] params = new Object[]{
                fieldValue
        };

        int returnedId = this.getJdbcTemplate().update(sql, params);

       return returnedId;
    }

    @Override
    public List<Event> getAllEvents() {

        String sql = "SELECT * FROM \"Event\"";
        List<Event> events = this.getJdbcTemplate().query(sql,new BeanPropertyRowMapper(Event.class));

        return events;
    }


}

