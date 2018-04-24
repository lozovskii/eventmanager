package com.ncgroup2.eventmanager.service.entityservice;

import com.ncgroup2.eventmanager.dao.impl.postgres.EventDaoImpl;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventDaoImpl eventDaoImpl;

    @Override
    public void createEvent(Event event) {
        eventDaoImpl.createEvent(event);
    }

    @Override
    public void changeVisibility(String visibilityType, Event event) {
        eventDaoImpl.updateField(event,"visibility", visibilityType);
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = eventDaoImpl.getAllEvents();
        return events;
    }
}
