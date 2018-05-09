package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.dto.AdditionalEventModelDTO;
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
    public void createEventWithoutTime(Event event, int visibility, int eventStatus, UUID groupId,
                                       int priorityId, UUID eventId) {

        String query = queryService.getQuery("event.createWithoutTime");
        Object[] eventParams = new Object[]{
                eventId,
                event.getName(),
                groupId,
                event.getCreatorId(),
                event.getStartTime(),
                event.getEndTime(),
                visibility,
                event.getDescription(),
                eventStatus
        };
        this.getJdbcTemplate().update(query, eventParams);
        createCustomerEvent(event, eventId, priorityId);
    }

    @Override
    public void createEvent(Event event, int visibility, int eventStatus, String frequency, UUID groupId,
                            int priorityId, UUID eventId) {

        String query = queryService.getQuery("event.create");
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
        this.getJdbcTemplate().update(query, eventParams);
        createCustomerEvent(event, eventId, priorityId);
    }

    @Override
    public void deleteEvent(Event event) {
        String query = queryService.getQuery("event.delete");
        Object[] params = new Object[]{
                event.getId()
        };
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public void deleteEventById(String eventId) {
        String query = queryService.getQuery("event.deleteById");
        Object[] params = new Object[]{
                eventId
        };
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public void updateEvent(Event event, String priority) {
        String event_query = queryService.getQuery("event.updateById");
        Object[] event_params = new Object[]{
                event.getName(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getId()
        };
        this.getJdbcTemplate().update(event_query, event_params);
        String customer_event_query = queryService.getQuery("customer_event.updatePriorityByEventId");
        Object[] customer_event_params = new Object[]{
                priority,
                event.getId()
        };
        this.getJdbcTemplate().update(customer_event_query, customer_event_params);
    }


    @Override
    public int getStatusId(String fieldValue) {
        String query = queryService.getQuery("event.getStatusId");
        Object[] params = new Object[]{
                fieldValue
        };
        return this.getJdbcTemplate().queryForObject(query, params, int.class);
    }

    @Override
    public int getVisibilityId(String fieldValue) {
        String query = queryService.getQuery("event.getVisibilityId");
        Object[] params = new Object[]{
                fieldValue
        };
        return this.getJdbcTemplate().queryForObject(query, params, int.class);
    }

    @Override
    public int getPrioriryId(String fieldValue) {
        String query = queryService.getQuery("event.getProirityId");
        Object[] params = new Object[]{
                fieldValue
        };
        return this.getJdbcTemplate().queryForObject(query, params, int.class);
    }

    @Override
    public List getEventsByCustId(String custId) {
        String query = queryService.getQuery("event.getEventsByCustId");
        Object[] params = new Object[]{
                custId
        };
        return this.getJdbcTemplate().query(query, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public List<Event> getNotesByCustId(String custId) {
        String query = queryService.getQuery("note.getByCustId");
        Object[] params = new Object[]{
                custId
        };
        return this.getJdbcTemplate().query(query, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public List<Event> getInvitesByCustId(String custId) {
        String query = queryService.getQuery("event.getInvitesByCustId");
        Object[] params = new Object[]{
                custId
        };
        return this.getJdbcTemplate().query(query, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public List<Event> getDraftsByCustId(String custId) {
        String query = queryService.getQuery("drafts.getByCustId");
        Object[] params = new Object[]{
                custId
        };
        return this.getJdbcTemplate().query(query, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public void createEventInvitation(String login, UUID eventId) {
        String query = queryService.getQuery("event.createInvitation");
        Object[] params = new Object[]{
                login,
                eventId
        };
        this.getJdbcTemplate().update(query, params);
    }

    @Override
    public String getTimeToEventStart(String eventId) {
        String query = queryService.getQuery("event.getTimeToStart");
        Object[] params = new Object[]{
                eventId
        };
        List<String> list = (List<String>) this.getJdbcTemplate().query(query, params,
                new BeanPropertyRowMapper(Event.class));
        return list.get(0);
    }

    @Override
    public Event getEventById(String eventId) {
        String query = queryService.getQuery("event.getEventById");
        Object[] params = new Object[]{
                eventId
        };
        List<Event> list = this.getJdbcTemplate().query(query, params, new BeanPropertyRowMapper(Event.class));
        if (!list.isEmpty()) {
            return list.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public AdditionalEventModelDTO getAdditionById(String eventId) {
        String query = queryService.getQuery("event.getAdditionById");
        Object[] params = new Object[]{
                eventId
        };
        List<AdditionalEventModelDTO> list = this.getJdbcTemplate().query(query, params,
                new BeanPropertyRowMapper(AdditionalEventModelDTO.class));
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
                "      AND end_time IS NOT NULL\n" +
                "ORDER BY start_time";

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

    private void createCustomerEvent(Event event, UUID eventId, int priorityId) {
        String query_customer_event = queryService.getQuery("customer_event.create");
        Object[] customerEventParams = new Object[]{
                eventId,
                event.getCreatorId(),
                event.getStartTime(),
                priorityId
        };
        this.getJdbcTemplate().update(query_customer_event, customerEventParams);
    }

}
