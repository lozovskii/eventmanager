package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.dto.EventDTO;
import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;

public interface EventService {

    void createEvent(EventDTO eventDTO);

    List<Event> getEventsByCustId(String custId);

    Event getEventById(String eventId);

    void deleteEvent(String eventId);

    List<Event> getAllPublicAndFriendsEvents(String customerId);

    boolean isParticipant(String customerId, String eventId);

    void removeParticipant(String customerId, String eventId);

    void addParticipant(String customerId, String eventId);
}
