package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.dto.EventDTO;
import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;

public interface EventService {

    void createEvent(Event event, EventDTO eventDTO);

    List<Event> getEventsByCustId(String custId);

    Event getEventById(String eventId);

    void deleteEvent(String eventId);
}
