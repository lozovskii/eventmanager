package com.ncgroup2.eventmanager.service.impl;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.dao.EventDao;
import com.ncgroup2.eventmanager.dto.*;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.entity.Location;
import com.ncgroup2.eventmanager.service.EventService;
import com.ncgroup2.eventmanager.service.sender.MyMailSender;
import com.ncgroup2.eventmanager.util.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class EventServiceImpl implements EventService {
    private final String CUSTOMER_EVENT_PRIORITY_LOW = "LOW";
    private final String EVENT_STATUS_EVENT = "EVENT";
    private final String EVENT_STATUS_NOTE = "NOTE";
    private final String EVENT_VISIBILITY_PRIVATE = "PRIVATE";

    private final MyMailSender mailSender;
    private final EventDao eventDao;
    private final CustomerDao customerDao;
    private final GoogleCalendarService googleCalendarService;
    private  final LocationServiceImpl locationService;

    @Autowired
    public EventServiceImpl(MyMailSender mailSender, EventDao eventDao, CustomerDao customerDao,
                            GoogleCalendarService googleCalendarService, LocationServiceImpl locationService) {
        this.mailSender = mailSender;
        this.eventDao = eventDao;
        this.customerDao = customerDao;
        this.googleCalendarService = googleCalendarService;
        this.locationService = locationService;

    }

    @Override
    public void createEvent(EventDTO eventDTO) {
        Event event = eventDTO.getEvent();
        Object[] frequency = checkDefaultCustEventFrequency(eventDTO);
        Long frequencyNumber = (Long) frequency[0];
        String frequencyPeriod = (String) frequency[1];

        String priority = checkDefaultCustEventPriority(eventDTO);
        String status = checkDefaultEventStatus(eventDTO);
        String visibility = checkDefaultCustEventVisibility(eventDTO);

        int priorityId = eventDao.getPrioriryId(priority);
        int statusId = eventDao.getStatusId(status);
        int visibilityId = eventDao.getVisibilityId(visibility);

        UUID groupId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();

        if (status.equals(EVENT_STATUS_EVENT)) {
            if ((frequencyNumber != null) && (frequencyPeriod != null)) {
                for (int i = 0; i <= 10; i++) {
                    String startFrequencyPeriod = frequencyPeriod;
                    frequencyPeriod = i * frequencyNumber + " " + frequencyPeriod;
                    eventId = UUID.randomUUID();
                    locationId = UUID.randomUUID();
                    createEventByTime(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId, eventId);
                    frequencyPeriod = startFrequencyPeriod;
                    List loginList = getExistingCustomers(eventDTO.getAdditionEvent().getPeople());
                    createEventInvitations(loginList, eventId);
                    addLocation(eventDTO, eventId, locationId);
                }
            } else {
                createEventByTime(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId, eventId);
                List loginList = getExistingCustomers(eventDTO.getAdditionEvent().getPeople());
                createEventInvitations(loginList, eventId);
                addLocation(eventDTO, eventId, locationId);
            }
        } else {
            if ((frequencyNumber != null) && (frequencyPeriod != null)) {
                frequencyPeriod = frequencyNumber + " " + frequencyPeriod;
                createEventByTime(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId, eventId);
                addLocation(eventDTO, eventId, locationId);

            } else {
                createEventByTime(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId, eventId);
                addLocation(eventDTO, eventId, locationId);

            }
        }
    }

    private void addLocation(EventDTO eventDTO, UUID eventId, UUID locationId) {
        if(eventDTO.getAdditionEvent().getLocation() != null) {
            eventDTO.getAdditionEvent().getLocation().setEvent_id(eventId.toString());
            eventDTO.getAdditionEvent().getLocation().setId(locationId.toString());
            locationService.create(eventDTO.getAdditionEvent().getLocation());
        }
    }

    @Override
    public void createEventInvitations(List<String> logins, UUID eventId) {
        logins.forEach(login -> {
            eventDao.createEventInvitation(login, eventId);
            sendInviteEmail(login, eventId);
        });
    }

    @Override
    public List<EventDTO> getEventsByCustId(String custId) {
        List<Event> events =  eventDao.getEventsByCustId(custId);
        return setAdditionForEachEvent(events, custId);
    }

    @Override
    public List<EventDTO> getAllPublicEventsInMonth(String custId) {
        List<Event> events =  eventDao.getAllPublicEventsInMonth(custId);
        return setAdditionForEachEvent(events, custId);
    }

    @Override
    public List<EventDTO> getAllPrivateEventsInMonth(String custId) {
        List<Event> events =  eventDao.getAllPrivateEventsInMonth(custId);
        return setAdditionForEachEvent(events, custId);
    }

    @Override
    public List<EventDTO> getAllFriendsEventsInMonth(String custId) {
        List<Event> events =  eventDao.getAllFriendsEventsInMonth(custId);
        return setAdditionForEachEvent(events, custId);
    }

    @Override
    public List<EventDTO> getEventsByCustIdSorted(String custId) {
        List<Event> events =  eventDao.getEventsByCustIdSorted(custId);
        return setAdditionForEachEvent(events,custId);
    }

    @Override
    public List<EventDTO> getAllEventsByCustId(String custId) {
        List<Event> events =  eventDao.getAllByCustId(custId);
        return setAdditionForEachEvent(events,custId);
    }

    @Override
    public List<EventDTO> getEventsByCustIdSortedByType(String custId) {
        List<Event> events =  eventDao.getEventsByCustIdSortedByType(custId);
        return setAdditionForEachEvent(events,custId);
    }

    @Override
    public List<EventDTO> getEventsByCustIdFilterByType(String custId, String type) {
        List<Event> events =  eventDao.getEventsByCustIdFilterByType(custId,type);
        return setAdditionForEachEvent(events,custId);
    }

    @Override
    public EventDTO getEventById(String eventId, String custId) {
        Event event = eventDao.getEventById(eventId);
        AdditionalEventModelDTO additionalEventModelDTO = eventDao.getAdditionById(eventId,custId);
        List<String> listParticipants = eventDao.getParticipants(eventId);
        Location location = locationService.getByEventId(eventId);
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEvent(event);
        if (listParticipants != null) {
            additionalEventModelDTO.setPeople(listParticipants);
        }
        if (location != null) {
            additionalEventModelDTO.setLocation(location);
        }
        eventDTO.setAdditionEvent(additionalEventModelDTO);
        return eventDTO;
    }

    @Override
    public EventDTO getNoteById(String noteId,String custId) {
        Event event = eventDao.getNoteById(noteId);
        AdditionalEventModelDTO additionalEventModelDTO = eventDao.getAdditionById(noteId,custId);
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEvent(event);
        eventDTO.setAdditionEvent(additionalEventModelDTO);
        return eventDTO;
    }

    @Override
    public void deleteEventById(String eventId) {
        locationService.delete(eventId);
        eventDao.deleteEventById(eventId);
    }

    @Override
    public void updateEventNotif(EventDTO eventDTO) {
        Event event = eventDTO.getEvent();
        LocalDateTime startNotifTime = eventDTO.getAdditionEvent().getStartTimeNotification();
        LocalDateTime startTime = eventDTO.getEvent().getStartTime();
        LocalDateTime currentTime = LocalDateTime.now();
        if (startNotifTime != null) {
            if (startNotifTime.isBefore(startTime)) {
                if (startNotifTime.isAfter(currentTime)) {
                    eventDao.updateStartNotifTime(event, startNotifTime);
                }
            }
        }
    }

    @Override
    public void updateEvent(UpdateEventDTO updateEventDTO) {
        Event event = updateEventDTO.getEvent();
        String priority = updateEventDTO.getPriority();
        eventDao.updateEvent(event, priority);
        Location location = updateEventDTO.getLocation();
            if (location != null) {
            locationService.update(location);
            }
        getExistingCustomers(updateEventDTO.getNewPeople()).
                forEach(login -> eventDao.createEventInvitation(login, UUID.fromString(event.getId())));

        updateEventDTO.getRemovedPeople().
                forEach(login -> eventDao.removeParticipant(login, event.getId()));
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
    public List<Event> getDraftsByCustId(String custId) {
        return eventDao.getDraftsByCustId(custId);
    }

    @Override
    public List<Event> getNotesByCustId(String custId) {
        return eventDao.getNotesByCustId(custId);
    }

    @Override
    public List<Event> getInvitesByCustId(String custId) {
        return eventDao.getInvitesByCustId(custId);
    }

    @Override
    public List<InviteNotificationDTO> getInviteNotifications(String customerId) {
        return eventDao.getInviteNotifications(customerId);
    }

    @Override
    public List<Event> getNationalEvents(String calendarId, LocalDateTime from, LocalDateTime to) throws Exception {
        return googleCalendarService.getEvents(calendarId, from, to);
    }

    @Override
    public List<EventDTO> getTimeline(String login, LocalDateTime from, LocalDateTime to) {
        String customerId = customerDao.getEntityByField("login", login).getId();
        List<Event> list =  eventDao.getTimelineEvents(customerId, from, to);
        List<EventDTO> dtoList = new ArrayList<>();
        list.forEach((event) -> {
            EventDTO dto = new EventDTO();
            dto.setEvent(event);
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public void updatePriority(String customerId, String eventId, String priority) {
        eventDao.updatePriority(customerId, eventId, priority);
    }

    private List<String> getExistingCustomers(List<String> logins) {
        return logins.stream()
                .filter(customerDao::isCustomerExist)
                .collect(Collectors.toList());
    }

    private void sendInviteEmail(String login, UUID eventId) {
        String sendTo = customerDao.getEntityByField("login", login).getEmail();
        Customer inviter = customerDao.getEntityByField("login", SecurityContextHolder.getContext().getAuthentication().getName());
        Event event = eventDao.getEventById(eventId.toString());

        String subject = "New invite";

        String template = "%s invited you to '%s' event.\n See more: ";
        String message = String.format(template, inviter.getLogin(), event.getName());

        String url = "/event-container/" + eventId.toString();
        mailSender.sendBasicEmailWithLink(sendTo, subject, message, url);
    }

    private Object[] checkDefaultCustEventFrequency(EventDTO eventDTO) {
        Long frequencyNumber = eventDTO.getAdditionEvent().getFrequencyNumber();
        String frequencyPeriod = eventDTO.getAdditionEvent().getFrequencyPeriod();
        Object[] list = new Object[2];
        if ((frequencyNumber != null) && (!frequencyPeriod.equals(""))) {
            frequencyPeriod = eventDTO.getAdditionEvent().getFrequencyPeriod();
            frequencyNumber = eventDTO.getAdditionEvent().getFrequencyNumber();
        }
        list[0] = frequencyNumber;
        list[1] = frequencyPeriod;
        return list;
    }

    private String checkDefaultCustEventPriority(EventDTO eventDTO) {
        String priority;
        if (!eventDTO.getAdditionEvent().getPriority().equals("")) {
            priority = eventDTO.getAdditionEvent().getPriority();
        } else {
            priority = CUSTOMER_EVENT_PRIORITY_LOW;
        }
        return priority;
    }

    private String checkDefaultEventStatus(EventDTO eventDTO) {
        String status;
        if (eventDTO.getEvent().getStatus() != null) {
            status = eventDTO.getEvent().getStatus();
        } else {
            if ((eventDTO.getEvent().getStartTime() != null) && (eventDTO.getEvent().getEndTime() != null)) {
                status = EVENT_STATUS_EVENT;
            } else {
                status = EVENT_STATUS_NOTE;
            }
        }
        return status;
    }

    private String checkDefaultCustEventVisibility(EventDTO eventDTO) {
        String visibility;
        if ((eventDTO.getEvent().getVisibility() == null) || (eventDTO.getEvent().getVisibility().equals(""))) {
            visibility = EVENT_VISIBILITY_PRIVATE;
        } else {
            visibility = eventDTO.getEvent().getVisibility();
        }
        return visibility;
    }

    private void createEventByTime(Event event, int visibilityId, int statusId, String frequencyPeriod,
                                   UUID groupId, int priorityId, UUID eventId) {
        if ((event.getStartTime() == null) && (event.getEndTime() == null)) {
            eventDao.createEventWithoutTime(event, visibilityId, statusId, groupId, priorityId,
                    eventId);
        } else {
            if ((frequencyPeriod == null) || (frequencyPeriod.equals(""))) {
                eventDao.createEventWithoutTime(event, visibilityId, statusId, groupId, priorityId,
                        eventId);
            } else {
                eventDao.createEvent(event, visibilityId, statusId, frequencyPeriod, groupId, priorityId, eventId);
            }
            eventDao.createChatForEvent(eventId);
        }
    }

    private List<EventDTO> setAdditionForEachEvent(List<Event> events, String custId) {
        List<EventPriorityDTO> idsPriorities = eventDao.getPriotityByCustId(custId);
        List<EventDTO> eventsDTO = new ArrayList<>();

        IntStream.range(0, events.size()).forEachOrdered(i -> {
            EventDTO eventDTO = new EventDTO();
            String id = events.get(i).getId();

            String priority = idsPriorities.stream()
                    .map(x -> {
                        if (x.getEventId().equals(id)) {
                            return x.getPriority();
                        }else{
                            return "";
                        }
                    }).collect(Collectors.joining(""));
            AdditionalEventModelDTO additionalEventModelDTO = new AdditionalEventModelDTO();
            additionalEventModelDTO.setPriority(priority);
            eventDTO.setEvent(events.get(i));
            eventDTO.setAdditionEvent(additionalEventModelDTO);
            eventsDTO.add(eventDTO);
        });
        return eventsDTO;
    }
}
