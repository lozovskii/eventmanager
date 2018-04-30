package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.dto.EventDTO;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventDao;

    @Override
    public void createEvent(Event event, EventDTO eventDTO) {
        System.out.println("dto = " + eventDTO.getFrequencyNumber());
        int visibilityId, statusId;
        if((event.getStartTime() != null) && (event.getEndTime() != null)) {
            statusId = eventDao.getStatusId("EVENT");
        }else{
            statusId = eventDao.getStatusId("NOTE");
        }
        if (event.getVisibility().equals("")) {
            visibilityId = eventDao.getVisibilityId("PRIVATE");
        }else{
            visibilityId = eventDao.getVisibilityId(event.getVisibility());
        }
        eventDao.createEvent(event, visibilityId, statusId);
    }

    @Override
    public List<Event> getEventsByCustId(String custId) {
        List<Event> events = eventDao.getEventsByCustId(custId);
        return events;
    }

    @Override
    public Event getEventById(String eventId) {
        return eventDao.getById(eventId);
    }

    @Override
    public void deleteEvent(String eventId) {
        eventDao.deleteEvent(eventId);
    }

}
