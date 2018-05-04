package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.dto.EventCountdownDTO;
import com.ncgroup2.eventmanager.dto.EventDTO;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventDao;

    @Override
    public void createEvent(EventDTO eventDTO) {
        int visibilityId, statusId, priorityId;
        String priority, status, visibility, frequencyPeriod;
        Long frequencyNumber;

        Event event = eventDTO.getEvent();
        if(eventDTO.getAdditionEvent().getFrequencyNumber() !=null){
            frequencyPeriod = eventDTO.getAdditionEvent().getFrequencyPeriod();
            frequencyNumber = eventDTO.getAdditionEvent().getFrequencyNumber();
        }else{
            frequencyPeriod = "day";
            frequencyNumber = new Long(0);
        }
        if(!eventDTO.getAdditionEvent().getPriority().equals("")){
            priority = eventDTO.getAdditionEvent().getPriority();
        }else {
            priority = "LOW";
        }
        if(eventDTO.getEvent().getStatus() !=null){
            status = eventDTO.getEvent().getStatus();
        }else{
            if((eventDTO.getEvent().getStartTime() != null) && (eventDTO.getEvent().getEndTime() != null)) {
                status = "EVENT";
            }else {
                status = "NOTE";
            }
        }
        if(eventDTO.getEvent().getVisibility() == null) {
            visibility = "PRIVATE";
        }else {
            visibility = eventDTO.getEvent().getVisibility();
        }

        priorityId = eventDao.getPrioriryId(priority);
        statusId = eventDao.getStatusId(status);
        visibilityId = eventDao.getVisibilityId(visibility);

        UUID groupId = UUID.randomUUID();
        if(status.equals("EVENT")) {
            for (int i = 0; i <= 10; i++) {
                String startFrequencyPeriod = frequencyPeriod;
                frequencyPeriod = i * frequencyNumber + " " + frequencyPeriod;
                eventDao.createEvent(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId);
                frequencyPeriod = startFrequencyPeriod;
            }
        }else{
            frequencyPeriod = frequencyNumber + " " + frequencyPeriod;
            eventDao.createEvent(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId);
        }
    }


    @Override
    public List<Event> getEventsByCustId(String custId) {
        return eventDao.getEventsByCustId(custId);
    }

    @Override
    public Event getEventById(String eventId) {
        return eventDao.getById(eventId);
    }

    @Override
    public void deleteEvent(String eventId) {
        eventDao.deleteEvent(eventId);
    }

    @Override
    public List<Event> getAllPublicAndFriendsEvents(String customerId) {
        return eventDao.getAllPublicAndFriends(customerId);
    }

    @Override
    public boolean isParticipant(String customerId, String eventId) {
        return eventDao.isParticipant(customerId, eventId);
    }

    @Override
    public void removeParticipant(String customerId, String eventId) {
        eventDao.removeParticipant(customerId, eventId);
    }

    @Override
    public void addParticipant(String customerId, String eventId) {
        eventDao.addParticipant(customerId, eventId);
    }

    public List<EventCountdownDTO> getCountdownMessages() {
        return eventDao.getCountdownMessages();
    }

    @Override
    public List<Event> getDraftsByCustId(String custId){
        return eventDao.getDraftsByCustId(custId);
    }

    @Override
    public List<Event> getNotesByCustId(String custId){
        return eventDao.getNotesByCustId(custId);
    }

}
