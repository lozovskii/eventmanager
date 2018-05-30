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

    void createChatForEvent(UUID eventId);

    void createEvent(Event event, int visibility, int eventStatus, String frequency, UUID groupId,
                     int priorityId, UUID eventId);

    void deleteEvent(Event event);

    void deleteEventById(String eventId);

    void updateEvent(Event event, String priority);

    int getStatusId(String fieldValue);

    int getVisibilityId(String fieldValue);

    int getPrioriryId(String fieldValue);

    Event getEventById(String id);

    Event getNoteById(String noteId);

    AdditionalEventModelDTO getAdditionById(String eventId, String custId);

    List getPriotityByCustId(String custId);

    List getParticipants(String eventId);

    List<Event> getAllPublicAndFriends(String customerId);

    List<InviteNotificationDTO> getInviteNotifications(String customerId);

    boolean isParticipant(String customerId, String eventId);

    void removeParticipant(String customerId, String eventId);

    void addParticipant(String customerId, String eventId);

    void createEventInvitation(String login, UUID eventId);

    List<EventCountdownDTO> getCountdownMessages();

    String getTimeToEventStart(String eventId);

    List getEventsByCustId(String custId);

    List getAllPublicEventsInMonth(String custId);
    List getAllPrivateEventsInMonth(String custId);
    List getAllFriendsEventsInMonth(String custId);

    List getEventsByCustIdSorted(String custId);

    List getAllByCustId(String custId);

    List getEventsByCustIdSortedByType(String custId);

    List getEventsByCustIdFilterByType(String custId, String type);

    List<Event> getEventsByCustIdSortedByStartTime(String custId);

    List<Event> getNotesByCustId(String custId);

    List<Event> getInvitesByCustId(String custId);

    List<Event> getDraftsByCustId(String custId);

    void updateStartNotifTime(Event event, LocalDateTime startNotifTime);

    List<Event> getTimelineEvents(String customerId, LocalDateTime from, LocalDateTime to);

    void updatePriority(String customerId, String eventId, String priority);

}
