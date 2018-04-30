package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;

public interface EventService {

    void createEvent(Event event);

    List<Event> getEventsByCustId(String custId);

}
