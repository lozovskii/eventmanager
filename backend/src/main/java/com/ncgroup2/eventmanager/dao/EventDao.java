package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.dto.EventCountdownDTO;
import com.ncgroup2.eventmanager.entity.Event;

import java.util.List;
import java.util.UUID;

public interface EventDao {

    void createEvent(Event event, int visibility, int eventStatus, String frequency, UUID groupId,
                     int priorityId);

    void deleteEvent(Event event);

    void deleteEvent(String eventId);

    void updateField(Event event, String fieldName, Object fieldValue);

    int getStatusId(String fieldValue);

    int getVisibilityId(String fieldValue);

    int getPrioriryId(String fieldValue);

    List<Event> getEventsByCustId(String custId);

    Event getById(String id);

    List<Event> getAllPublicAndFriends(String customerId);

    boolean isParticipant(String customerId, String eventId);

    void removeParticipant(String customerId, String eventId);

    void addParticipant(String customerId, String eventId);

    List<EventCountdownDTO> getCountdownMessages();

    void saveEventAsADraft(String eventId);

    String getTimeToEventStart(String eventId);

    List<Event> getNotesByCustId(String custId);
}
