package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;

public interface EventDao {

    void createEvent(Event event);

    void deleteEvent(Event event);

    void updateField(Event event, String fieldName, Object fieldValue);

    int getIdByField(String fieldName, String fieldValue);

    List<Event> getAllEvents();

    List<Event> getEventsByCustId(String custId);

    void addEventParticipants(String eventId, String custId);
}
