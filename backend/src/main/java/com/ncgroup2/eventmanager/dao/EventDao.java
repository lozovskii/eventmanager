package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Event;
import java.util.List;

public interface EventDao {

    void createEvent(Event event, int visibility, int eventStatus);

    void deleteEvent(Event event);

    void updateField(Event event, String fieldName, Object fieldValue);

    int getStatusId(String fieldValue);

    int getVisibilityId(String fieldValue);

    List<Event> getEventsByCustId(String custId);

    Event getById(String id);

    List<Event> getAllPublicAndFriends(String customerId);
}
