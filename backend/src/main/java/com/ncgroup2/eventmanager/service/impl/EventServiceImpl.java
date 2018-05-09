package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.dto.EventCountdownDTO;
import com.ncgroup2.eventmanager.dto.EventDTO;
import com.ncgroup2.eventmanager.dto.InviteNotificationDTO;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class EventServiceImpl implements EventService {
    private final String CUSTOMER_EVENT_PRIORITY_LOW = "LOW";
    private final String EVENT_STATUS_EVENT = "EVENT";
    private final String EVENT_STATUS_NOTE = "NOTE";
    private final String EVENT_VISIBILITY_PRIVATE = "PRIVATE";
    private final String CUSTOMER_EVENT_FREQUENCY_PERIOD_DAY = "day";

    @Autowired
    private EventDao eventDao;
    @Autowired
    private CustomerDao customerDao;

    @Override
    public void createEvent(EventDTO eventDTO) {
        Event event = eventDTO.getEvent();
        Object[] frequancy = checkDefaultCustEventFrequency(eventDTO);
        Long frequencyNumber = (Long)frequancy[0];
        String frequencyPeriod = (String)frequancy[1];

        String priority = checkDefaultCustEventPriority(eventDTO);
        String status = checkDefaultEventStatus(eventDTO);
        String visibility = checkDefaultCustEventVisibility(eventDTO);

        int priorityId = eventDao.getPrioriryId(priority);
        int statusId = eventDao.getStatusId(status);
        int visibilityId = eventDao.getVisibilityId(visibility);

        UUID groupId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        if(status.equals(EVENT_STATUS_EVENT)) {
            if((frequencyNumber != null) && (frequencyPeriod != null)){
                for (int i = 0; i <= 10; i++) {
                    String startFrequencyPeriod = frequencyPeriod;
                    frequencyPeriod = i * frequencyNumber + " " + frequencyPeriod;
                    eventId = UUID.randomUUID();
                    createEventByTime(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId, eventId);
                    frequencyPeriod = startFrequencyPeriod;
                }
            }else{
                createEventByTime(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId,eventId);
            }
        }else{
            if((frequencyNumber != null) && (frequencyPeriod != null)) {
                frequencyPeriod = frequencyNumber + " " + frequencyPeriod;
                createEventByTime(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId, eventId);
            }else{
                createEventByTime(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId,eventId);
            }
        }
        List loginList = isCustomersExist(eventDTO.getAdditionEvent().getPeople());
        createEventInvitations(loginList,eventId);
    }

    @Override
    public List isCustomersExist(List<String> login) {
        List<String> verifiedCustomers = new ArrayList<>();
        IntStream.range(0, login.size()).forEach(i -> {
            boolean custStatus = customerDao.isCustomerExist(login.get(i));
            if (custStatus) {
                verifiedCustomers.add(login.get(i));
            }
        });
        return verifiedCustomers;
    }

    @Override
    public void createEventInvitations(List<String> login, UUID eventId) {
        IntStream.range(0, login.size()).forEach(i -> eventDao.createEventInvitation(login.get(i), eventId));
    }

    @Override
    public List<Event> getEventsByCustId(String custId) {
        return eventDao.getEventsByCustId(custId);
    }

    @Override
    public Event getEventById(String eventId) {
        return eventDao.getById(eventId);
    }

    public void deleteEventById(String eventId) {
        eventDao.deleteEventById(eventId);
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
    public void addParticipant(String customerId, String eventId, Instant startDateNotification, int priority) {
        eventDao.addParticipant(customerId, eventId, startDateNotification, priority);
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

    @Override
    public List<Event> getInvitesByCustId(String custId){
        return eventDao.getInvitesByCustId(custId);
    }

    @Override
    public List<InviteNotificationDTO> getInviteNotifications(String customerId) {
        return eventDao.getInviteNotifications(customerId);
    }


    private Object[] checkDefaultCustEventFrequency(EventDTO eventDTO) {
        Long frequencyNumber = eventDTO.getAdditionEvent().getFrequencyNumber();
        String frequencyPeriod = eventDTO.getAdditionEvent().getFrequencyPeriod();
        Object[] list = new Object[2];
        if((frequencyNumber!=null)&&(!frequencyPeriod.equals(""))){
            frequencyPeriod = eventDTO.getAdditionEvent().getFrequencyPeriod();
            frequencyNumber = eventDTO.getAdditionEvent().getFrequencyNumber();
        }
        list[0] = frequencyNumber;
        list[1] = frequencyPeriod;
        return list;
    }

    private String checkDefaultCustEventPriority(EventDTO eventDTO) {
        String priority;
        if(!eventDTO.getAdditionEvent().getPriority().equals("")){
            priority = eventDTO.getAdditionEvent().getPriority();
        }else {
            priority = CUSTOMER_EVENT_PRIORITY_LOW;
        }
        return priority;
    }

    private String checkDefaultEventStatus(EventDTO eventDTO) {
        String status;
        if(eventDTO.getEvent().getStatus() !=null){
            status = eventDTO.getEvent().getStatus();
        }else{
            if((eventDTO.getEvent().getStartTime() != null) && (eventDTO.getEvent().getEndTime() != null)) {
                status = EVENT_STATUS_EVENT;
            }else {
                status = EVENT_STATUS_NOTE;
            }
        }
        return status;
    }

    private String checkDefaultCustEventVisibility(EventDTO eventDTO) {
        String visibility;
        if(eventDTO.getEvent().getVisibility() == null) {
            visibility = EVENT_VISIBILITY_PRIVATE;
        }else {
            visibility = eventDTO.getEvent().getVisibility();
        }
        return visibility;
    }

    private void createEventByTime(Event event, int visibilityId, int statusId, String frequencyPeriod,
                                   UUID groupId, int priorityId, UUID eventId) {
        if((event.getStartTime() == null) && (event.getEndTime() == null)) {
            eventDao.createEventWithoutTime(event, visibilityId, statusId, groupId, priorityId,
                        eventId);
        }else{
            if((frequencyPeriod == null) || (frequencyPeriod.equals(""))){
                eventDao.createEventWithoutTime(event, visibilityId, statusId, groupId, priorityId,
                        eventId);
            }else {
                eventDao.createEvent(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId, eventId);
            }
        }
    }

}
