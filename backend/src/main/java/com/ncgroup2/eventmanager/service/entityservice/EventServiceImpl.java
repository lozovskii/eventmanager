package com.ncgroup2.eventmanager.service.entityservice;

import com.ncgroup2.eventmanager.dao.impl.postgres.EventDaoImpl;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ncgroup2.eventmanager.entity.Event;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventDaoImpl eventDaoImpl;

    @Override
    public Event createEvent(Event event) {
        eventDaoImpl.createEvent(event);
        return null;
    }
}
