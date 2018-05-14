package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.dto.AdditionalEventModelDTO;
import com.ncgroup2.eventmanager.dto.EventCountdownDTO;
import com.ncgroup2.eventmanager.dto.InviteNotificationDTO;
import com.ncgroup2.eventmanager.entity.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventDao {

    void createEventWithoutTime(Event event, int visibility, int eventStatus, UUID groupId,
                                int priorityId, UUID eventId);

    void createEvent(Event event, int visibility, int eventStatus, String frequency, UUID groupId,
                     int priorityId, UUID eventId);

    void deleteEvent(Event event);

    void deleteEventById(String eventId);

    void updateEvent(Event event, String priority);

    int getStatusId(String fieldValue);

    int getVisibilityId(String fieldValue);

    int getPrioriryId(String fieldValue);

    List getEventsByCustId(String custId);

    Event getEventById(String id);

    Event getNoteById(String noteId);

    AdditionalEventModelDTO getAdditionById(String id);

    List getParticipants(String eventId);

    List<Event> getAllPublicAndFriends(String customerId);

    List<InviteNotificationDTO> getInviteNotifications(String customerId);

    boolean isParticipant(String customerId, String eventId);

    void removeParticipant(String customerId, String eventId);

    void addParticipant(String customerId, String eventId);

    void createEventInvitation(String login, UUID eventId);

    List<EventCountdownDTO> getCountdownMessages();

    String getTimeToEventStart(String eventId);

    List getEventsByCustIdSorted(String custId);

    List getEventsByCustIdSortedByType(String custId);

    List<Event> getNotesByCustId(String custId);

    List<Event> getInvitesByCustId(String custId);

    List<Event> getDraftsByCustId(String custId);

    void updateStartNotifTime(Event event, LocalDateTime startNotifTime);
}
