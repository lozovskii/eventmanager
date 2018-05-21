package com.ncgroup2.eventmanager.dao.impl;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.dto.AdditionalEventModelDTO;
import com.ncgroup2.eventmanager.dto.EventCountdownDTO;
import com.ncgroup2.eventmanager.dto.InviteNotificationDTO;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.util.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public class EventDaoImpl extends JdbcDaoSupport implements EventDao {

    @Autowired
    DataSource dataSource;
    @Autowired
    private QueryService queryService;

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
        String query = queryService.getQuery("event.getByCustIdSort");
        Object[] params = new Object[]{
                custId,
                custId
        };
        String orderBy = " ORDER BY start_time";
        return this.getJdbcTemplate().query(query+orderBy, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public List getEventsByCustIdSorted(String custId) {
        String query = queryService.getQuery("event.getByCustIdSort");
        Object[] params = new Object[]{
                custId,
                custId
        };
        String orderBy = " ORDER BY name";
        return this.getJdbcTemplate().query(query+orderBy, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public List getEventsByCustIdSortedByType(String custId) {
        String query = queryService.getQuery("event.getByCustIdSort");
        Object[] params = new Object[]{
                custId,
                custId
        };
        String orderBy = " ORDER BY status";
        return this.getJdbcTemplate().query(query+orderBy, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public List getEventsByCustIdFilterByType(String custId, String type) {
        String query = queryService.getQuery("event.getByCustIdFilterByType");
        Object[] params = new Object[]{
                custId,
                type,
                custId,
                type
        };
        return this.getJdbcTemplate().query(query, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public List<Event> getEventsByCustIdSortedByStartTime(String custId) {
        String query = queryService.getQuery("event.getByCustIdSortByStartDate");
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
        String query = queryService.getQuery("event.getById");
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
    public Event getNoteById(String noteId) {
        String query = queryService.getQuery("note.getById");
        Object[] params = new Object[]{
                noteId
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
        List<AdditionalEventModelDTO> listAddition = this.getJdbcTemplate().query(query, params,
                new BeanPropertyRowMapper(AdditionalEventModelDTO.class));
        if (!listAddition.isEmpty()) {
            return listAddition.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public List getParticipants(String eventId) {
        String query = queryService.getQuery("event.getParticipants");
        Object[] params = new Object[]{
                eventId
        };
        List<String> listParticipants = this.getJdbcTemplate().queryForList(query, params, String.class);
        if (!listParticipants.isEmpty()) {
            return listParticipants;
        } else {
            return null;
        }
    }

    @Override
    public List<Event> getAllPublicAndFriends(String customerId) {
        String sql = queryService.getQuery("event.public_and_friends");

        Object[] params = new Object[]{
                customerId,
                customerId
        };
        return this.getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper(Event.class));
    }

    @Override
    public List<InviteNotificationDTO> getInviteNotifications(String customerId) {
        String query = queryService.getQuery("customer_event.getInvites");
        Object[] params = new Object[]{
                customerId
        };
        return this.getJdbcTemplate().query(query, params, (resultSet, i) -> {
            InviteNotificationDTO notification = new InviteNotificationDTO();
            notification.setEventId(resultSet.getString("event_id"));
            notification.setEventName(resultSet.getString("event_name"));
            notification.setInviter(resultSet.getString("name") + " " + resultSet.getString("second_name"));
            return notification;
        });
    }

    @Override
    public boolean isParticipant(String customerId, String eventId) {
        String sql = queryService.getQuery("customer_event.is_participant");
        Object[] params = new Object[]{
                eventId,
                customerId
        };
        List<Boolean> list = this.getJdbcTemplate().query(sql, params, (resultSet, i) -> resultSet.next());
        return !list.isEmpty();
    }

    @Override
    public void removeParticipant(String customerId, String eventId) {
        String sql = queryService.getQuery("customer_event.remove_participant");
        Object[] params = new Object[]{
                customerId,
                eventId
        };
        this.getJdbcTemplate().update(sql, params);
    }

    @Override
    public void addParticipant(String customerId, String eventId) {
        boolean isPresent = isCustomerEventPresent(customerId, eventId);
        if (isPresent) {
            updateParticipant(customerId, eventId);
        } else {
            insertParticipant(customerId, eventId);
        }
    }

    private void insertParticipant(String customerId, String eventId) {
        String sql = queryService.getQuery("customer_event.insert_participant");
        Object[] params = new Object[]{
                customerId,
                eventId,
                eventId
        };
        this.getJdbcTemplate().update(sql, params);
    }

    private void updateParticipant(String customerId, String eventId) {
        String sql = queryService.getQuery("customer_event.update_participant");
        Object[] params = new Object[]{
                customerId,
                eventId
        };

        this.getJdbcTemplate().update(sql, params);
    }

    private boolean isCustomerEventPresent(String customerId, String eventId) {
        String sql = queryService.getQuery("customer_event.is_present");
        Object[] params = {
                customerId,
                eventId
        };
        List<String> result = this.getJdbcTemplate().query(sql, params, (rs, i) -> rs.getString("id"));
        return !result.isEmpty();
    }

    @Override
    public List<EventCountdownDTO> getCountdownMessages() {
        String sql = queryService.getQuery("customer_event.countdown_messages");
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

    @Override
    public void updateStartNotifTime(Event event, LocalDateTime startNotifTime) {
        String query_customer_event = queryService.getQuery("customer_event.updateNotifTime");
        Object[] customerEventParams = new Object[]{
                startNotifTime,
                event.getId()
        };
        this.getJdbcTemplate().update(query_customer_event, customerEventParams);
    }

    @Override
    public List<Event> getTimelineEvents(String customerId, LocalDateTime from, LocalDateTime to) {
        String sql = queryService.getQuery("event.timeline");
        Object[] params = new Object[] {
                customerId,
                from,
                to
        };
        return this.getJdbcTemplate().query(sql, params, (resultSet, i) -> {
            Event event = new Event();
            LocalDateTime eventTime = resultSet.getDate("date").toLocalDate().atStartOfDay();
            System.out.println(eventTime);
            event.setStartTime(eventTime);
            event.setEndTime(eventTime.plusDays(1).minusSeconds(1));
            if(resultSet.getInt("busy")>0) {
                event.setVisibility("PUBLIC");
            } else {
                event.setVisibility("PRIVATE");
            }
            return event;
        });
    }

}
