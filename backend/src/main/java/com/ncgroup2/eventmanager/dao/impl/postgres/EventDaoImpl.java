package com.ncgroup2.eventmanager.dao.impl.postgres;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.util.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

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

        UUID groupId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        String query_event = "INSERT INTO \"Event\" " +
                "(id,name,group_id,folder_id,creator_id,start_time,end_time,visibility,description,status) " +
                "VALUES(?,?, ?, null, CAST(? AS uuid),?,?,(SELECT id FROM \"Event_Visibility\" WHERE name = \'PRIVATE\'),?," +
                "(SELECT id FROM \"Event_Status\" WHERE name = \'EVENT\'));";

//        String query = queryService.getQuery("create.event");

        Object[] eventParams = new Object[]{
                eventId,
                event.getName(),
                groupId,
                event.getCreatorId(),
                event.getStartTime(),
                event.getEndTime(),
                event.getDescription()
        };
        this.getJdbcTemplate().update(query_event, eventParams);

        String query_customer_event = "INSERT INTO \"Customer_Event\"" +
                "(id,event_id,customer_id,start_date_notification,frequency_value,priority,status)" +
                "VALUES(uuid_generate_v1(),?,CAST(? AS uuid),?,1,(SELECT id FROM \"Customer_Event_Priority\" WHERE name = 'LOW')," +
                "(SELECT id FROM \"Customer_Event_Status\" WHERE name = 'ACCEPTED'))";

        Object[] customerEventParams = new Object[]{
                eventId,
                event.getCreatorId(),
                event.getStartTime()
        };
        this.getJdbcTemplate().update(query_customer_event, customerEventParams);
    }


    @Override
    public void deleteEvent(Event event) {
        String query = "DELETE FROM Event WHERE id = CAST (? AS UUID)";

        Object[] params = new Object[]{
                event.getId()
        };

        this.getJdbcTemplate().update(query, params);
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
        List<Event> events = this.getJdbcTemplate().query(sql, new BeanPropertyRowMapper(Event.class));

        return events;
    }

    @Override
    public List<Event> getEventsByCustId(String custId) {

        String sql = "select \"Event\".name as name, start_time, end_time, \"Event\".description as description," +
                     " \"Event_Visibility\".name as visibility " +
                     "from (\"Event\" INNER JOIN \"Event_Visibility\"" +
                     " ON \"Event\".visibility = \"Event_Visibility\".id) " +
                     "WHERE creator_id = CAST(? AS uuid)" +
                     "AND start_time IS NOT NULL " +
                     "AND end_time IS NOT NULL ";

        Object[] params = new Object[]{
                custId
        };
        List<Event> events = this.getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper(Event.class));
        return events;
    }

    @Override
    public void addEventParticipants(String eventId, String custId) {

    }


}

