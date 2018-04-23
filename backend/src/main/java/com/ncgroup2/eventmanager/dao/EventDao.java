package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Event;

public interface EventDao {

    void createEvent(Event event);

    void deleteEvent(Event event);

    void updateField(Event event, String fieldName, Object fieldValue);
}
