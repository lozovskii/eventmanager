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
        String priority, status, visibility;

        Event event = eventDTO.getEvent();
        String frequencyPeriod = eventDTO.getFrequencyPeriod();
        Long frequencyNumber = eventDTO.getFrequencyNumber();

        if(!eventDTO.getPriority().equals("")){
            priority = eventDTO.getPriority();
        }else {
            priority = "LOW";
        }
        if((event.getStartTime() != null) && (event.getEndTime() != null)) {
            status = "EVENT";
        }else {
            status = "NOTE";
        }
        if (event.getVisibility().equals("")) {
            visibility = "PRIVATE";
        }else {
            visibility = event.getVisibility();
        }

        priorityId = eventDao.getPrioriryId(priority);
        statusId = eventDao.getStatusId(status);
        visibilityId = eventDao.getVisibilityId(visibility);

        UUID groupId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        for (int i = 0; i <=frequencyNumber ; i++){
            frequencyPeriod = i + " " + frequencyPeriod;
            if(i == 0){
                eventId = groupId;
            }
            eventDao.createEvent(event, visibilityId, statusId, frequencyPeriod, groupId, eventId, priorityId);
        }
    }

    @Override
    public List<Event> getEventsByCustId(String custId) { return eventDao.getEventsByCustId(custId); }

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
    public void saveEventAsADraft(String eventId){
        eventDao.saveEventAsADraft(eventId);
    }

}
