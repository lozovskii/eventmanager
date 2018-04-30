package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.impl.EventDaoImpl;
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
        int visibilityId, statusId;
        if((event.getStartTime() != null) && (event.getEndTime() != null)) {
            statusId = eventDaoImpl.getStatusId("EVENT");
        }else{
            statusId = eventDaoImpl.getStatusId("NOTE");
        }
        if (event.getVisibility().equals("")) {
            visibilityId = eventDaoImpl.getVisibilityId("PRIVATE");
        }else{
            visibilityId = eventDaoImpl.getVisibilityId(event.getVisibility());
        }
        eventDaoImpl.createEvent(event, visibilityId, statusId);
    }

    @Override
    public List<Event> getEventsByCustId(String custId) {
        List<Event> events = eventDaoImpl.getEventsByCustId(custId);
        return events;
    }

    @Override
    public Event getEventById(String eventId) {
        return eventDaoImpl.getById(eventId);
    }

}
