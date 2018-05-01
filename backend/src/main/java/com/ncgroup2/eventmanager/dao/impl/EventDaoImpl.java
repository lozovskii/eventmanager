package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.dto.EventCountdownDTO;
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
    public void createEvent(Event event, int visibility, int eventStatus, String frequency, UUID groupId,
                            int priorityId) {

        UUID eventId = UUID.randomUUID();
        System.out.println("frequency = " + frequency);
        String query_event = "INSERT INTO \"Event\" " +
                "(id,name,group_id,folder_id,creator_id,start_time,end_time,visibility,description,status) " +
                "VALUES(?,?, ?, NULL, CAST(? AS UUID),(? + ?::interval)," +
                "(? + ?::interval),?,?,?)";

        Object[] eventParams = new Object[]{
                eventId,
                event.getName(),
                groupId,
                event.getCreatorId(),
                event.getStartTime(),
                frequency,
                event.getEndTime(),
                frequency,
                visibility,
                event.getDescription(),
                eventStatus
        };
        this.getJdbcTemplate().update(query_event, eventParams);

        String query_customer_event = "INSERT INTO \"Customer_Event\"" +
                "(id,event_id,customer_id,start_date_notification,frequency_value,priority,status)" +
                "VALUES(uuid_generate_v1(),?,CAST(? AS UUID),?,1,?," +
                "(SELECT id FROM \"Customer_Event_Status\" WHERE name = 'ACCEPTED'))";

        Object[] customerEventParams = new Object[]{
                eventId,
                event.getCreatorId(),
                event.getStartTime(),
                priorityId
        };
        this.getJdbcTemplate().update(query_customer_event, customerEventParams);
    }

    @Override
    public void deleteEvent(Event event) {
        String query = "DELETE FROM \"Event\" WHERE id = CAST (? AS UUID)";
        Object[] params = new Object[]{
                event.getId()
        };
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public void deleteEvent(String eventId) {
        String sql = "UPDATE \"Event\" SET status = (SELECT id FROM \"Event_Status\" WHERE name = 'DELETED') " +
                "WHERE id = CAST (? AS UUID)";
        Object[] params = new Object[]{
                eventId
        };
        this.getJdbcTemplate().update(sql, params);
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
    public int getStatusId(String fieldValue) {
        String sql = "SELECT id FROM \"Event_Status\" WHERE name = ?";
        Object[] params = new Object[]{
                fieldValue
        };
        return this.getJdbcTemplate().queryForObject(sql, params, int.class);
    }

    @Override
    public int getVisibilityId(String fieldValue) {
        String sql = "SELECT id FROM \"Event_Visibility\" WHERE name = ?";
        Object[] params = new Object[]{
                fieldValue
        };
        return this.getJdbcTemplate().queryForObject(sql, params, int.class);
    }

    @Override
    public int getPrioriryId(String fieldValue) {
        String sql = "SELECT id FROM \"Customer_Event_Priority\" WHERE name = ?";
        Object[] params = new Object[]{
                fieldValue
        };
        return this.getJdbcTemplate().queryForObject(sql, params, int.class);
    }

    @Override
    public List getEventsByCustId(String custId) {
        String sql = "SELECT \"Event\".id as id, \"Event\".name AS name, start_time, end_time, \"Event\".description AS description, " +
                "\"Event_Visibility\".name AS visibility, \"Event_Status\".name AS status " +
                "FROM (\"Event\" INNER JOIN \"Event_Visibility\" " +
                "ON \"Event\".visibility = \"Event_Visibility\".id) " +
                "INNER JOIN \"Event_Status\" " +
                "ON \"Event\".status = \"Event_Status\".id " +
                "WHERE creator_id = CAST(? AS UUID) " +
                "AND start_time IS NOT NULL " +
                "AND end_time IS NOT NULL " +
                "AND \"Event_Status\".name <> 'DELETED' " +
                "AND \"Event_Status\".name <> 'DRAFT'";
        Object[] params = new Object[]{
                custId
        };
        return this.getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public Event getById(String id) {
        String sql = "SELECT \"Event\".id AS id, \"Event\".creator_id AS creator_id, \"Event\".name AS name, " +
                "start_time, end_time, \"Event\".description AS description," +
                "\"Event_Visibility\".name AS visibility " +
                "FROM (\"Event\" INNER JOIN \"Event_Visibility\" " +
                "ON \"Event\".visibility = \"Event_Visibility\".id) " +
                "WHERE \"Event\".id  = CAST(? AS UUID) " +
                "AND start_time IS NOT NULL " +
                "AND end_time IS NOT NULL";

        Object[] params = new Object[]{
                id
        };
        List<Event> list = this.getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper(Event.class));
        if (!list.isEmpty()) {
            return list.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public List<Event> getAllPublicAndFriends(String customerId) {
        String sql = "SELECT\n" +
                "  \"Event\".id              AS id,\n" +
                "  \"Event\".name            AS name,\n" +
                "  start_time,\n" +
                "  end_time,\n" +
                "  \"Event\".description     AS description,\n" +
                "  \"Event_Visibility\".name AS visibility\n" +
                "FROM (\"Event\"\n" +
                "  INNER JOIN \"Event_Visibility\"\n" +
                "    ON \"Event\".visibility = \"Event_Visibility\".id)\n" +
                "WHERE \"Event\".visibility = (SELECT id\n" +
                "                            FROM \"Event_Visibility\"\n" +
                "                            WHERE name = 'PUBLIC')\n" +
                "      AND start_time IS NOT NULL\n" +
                "      AND end_time IS NOT NULL\n" +
                "\n" +
                "UNION\n" +
                "\n" +
                "SELECT\n" +
                "  \"Event\".id              AS id,\n" +
                "  \"Event\".name            AS name,\n" +
                "  start_time,\n" +
                "  end_time,\n" +
                "  \"Event\".description     AS description,\n" +
                "  \"Event_Visibility\".name AS visibility\n" +
                "FROM (\"Event\"\n" +
                "  INNER JOIN \"Event_Visibility\"\n" +
                "    ON \"Event\".visibility = \"Event_Visibility\".id)\n" +
                "WHERE \"Event\".visibility = (SELECT id\n" +
                "                            FROM \"Event_Visibility\"\n" +
                "                            WHERE name = 'FRIENDS')\n" +
                "\n" +
                "       AND (\"isFriends\"(creator_id, cast(? AS UUID))\n" +
                "           OR creator_id = cast(? AS UUID))\n" +
                "\n" +
                "\n" +
                "      AND start_time IS NOT NULL\n" +
                "      AND end_time IS NOT NULL;\n";

        Object[] params = new Object[]{
                customerId,
                customerId
        };
        return this.getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public boolean isParticipant(String customerId, String eventId) {
        String sql = "SELECT * FROM \"Customer_Event\" WHERE event_id = cast (? AS UUID)" +
                " AND customer_id = cast(? AS UUID)";
        Object[] params = new Object[]{
                eventId,
                customerId
        };
        List<Boolean> list = this.getJdbcTemplate().query(sql, params, (resultSet, i) -> resultSet.next());
        return !list.isEmpty();
    }

    @Override
    public void removeParticipant(String customerId, String eventId) {
        String sql = "DELETE FROM \"Customer_Event\" WHERE customer_id = cast(? AS UUID) " +
                "AND event_id = cast(? AS UUID)";
        Object[] params = new Object[]{
                customerId,
                eventId
        };
        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public void addParticipant(String customerId, String eventId) {
        String sql = "INSERT INTO \"Customer_Event\" (customer_id, event_id, status, priority)" +
                " VALUES (cast(? AS UUID), cast(? AS UUID), (SELECT id FROM \"Customer_Event_Status\" " +
                "WHERE name = 'ACCEPTED')," +
                "(SELECT id FROM \"Customer_Event_Priority\" WHERE name = 'AVERAGE'))";
        Object[] params = new Object[]{
                customerId,
                eventId
        };
        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public List<EventCountdownDTO> getCountdownMessages() {
        String sql = "SELECT\n" +
                "  (SELECT email\n" +
                "   FROM \"Customer\"\n" +
                "   WHERE id = ce.customer_id) AS email,\n" +
                "  event_countdown_message(event_id) AS message\n" +
                "FROM \"Customer_Event\" ce\n" +
                "WHERE date_trunc('day', start_date_notification) <= date_trunc('day', now()) AND\n" +
                "      ce.event_id = (SELECT id\n" +
                "                     FROM \"Event\"\n" +
                "                     WHERE\n" +
                "                       start_time > now() AND\n" +
                "                       ce.event_id = \"Event\".id)";
        return this.getJdbcTemplate().query(sql, (resultSet, i) -> {
            EventCountdownDTO countdownDTO = new EventCountdownDTO();
            countdownDTO.setEmail(resultSet.getString("email"));
            countdownDTO.setMessage(resultSet.getString("message"));
            return countdownDTO;
        });
    }

    @Override
    public void saveEventAsADraft(String eventId) {
        String sql = "UPDATE \"Event\" SET status = (SELECT id FROM \"Event_Status\" " +
                "WHERE \"Event_Status\".name = 'DRAFT') WHERE id = ?";
        Object[] params = new Object[]{
                eventId
        };
        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public String getTimeToEventStart(String eventId) {
        String sql = "SELECT (start_time - localtimestamp) FROM \"Event\" WHERE id = ?";
        Object[] params = new Object[]{
                eventId
        };
        List<String> query = (List<String>) this.getJdbcTemplate().query(sql, params,
                new BeanPropertyRowMapper(Event.class));
        return query.get(0);
    }

    @Override
    public List<Event> getNotesByCustId(String custId) {
        String sql = "SELECT \"Event\".id AS id,\"Event\".name AS name,\"Event\".description AS description " +
                "FROM \"Event\" WHERE status = (SELECT id FROM \"Event_Status\" " +
                "WHERE \"Event_Status\".name = 'NOTE') AND id = ?";
        Object[] params = new Object[]{
                custId
        };
        return this.getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper(Event.class));
    }


}

