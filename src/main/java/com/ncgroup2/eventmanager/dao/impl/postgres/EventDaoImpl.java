package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Relationship;
import com.ncgroup2.eventmanager.mapper.CustomerMapper;
import com.ncgroup2.eventmanager.mapper.EventMapper;
import com.ncgroup2.eventmanager.mapper.RelationshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

public class EventDaoImpl extends JdbcDaoSupport implements EventDao {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void createEvent(String creatorId, Event event) {

        String query = "INSERT INTO \"Event\" " +
                     "(id,name,folder_id, creator_id ,start_time,end_time,priority,isPublic,frequency,status)" +
                     "VALUES(uuid_generate_v1(),?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{
                event.getName(),
                null,
                creatorId,
                event.getStartTime(),
                event.getEndTime(),
                event.getPriority(),
                event.isPublic(),
                event.getFrequency(),
                event.getStatus()
        };

        this.getJdbcTemplate().update(query,params);

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

    public List<Event> events(String login) {

        String sql = "SELECT * FROM Event WHERE creator_id = 'd7638c26-4d85-11e8-bb3c-28d244397e45'";
        return this.getJdbcTemplate().query(sql, new EventMapper());
    }


}
