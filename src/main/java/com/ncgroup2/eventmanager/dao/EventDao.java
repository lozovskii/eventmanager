package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;

public interface EventDao {

    void createEvent(String creatorId, Event event);

    void deleteEvent(Event event);

    void updateField(Event event, String fieldName, Object fieldValue);

    List<Event> events(String login);
}
