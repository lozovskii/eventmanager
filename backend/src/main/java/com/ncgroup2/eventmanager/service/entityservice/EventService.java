package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;

public interface EventService {

    void createEvent(Event event);

    void changeVisibility(String visibilityType, Event event);

    List<Event> getAllEvents();

    List<Event> getEventsByCustId(String custId);
}
