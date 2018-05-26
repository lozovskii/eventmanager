package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.dto.EventDTO;
import com.ncgroup2.eventmanager.dto.InviteNotificationDTO;
import com.ncgroup2.eventmanager.dto.UpdateEventDTO;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public void create(@RequestBody EventDTO eventDTO){
        eventService.createEvent(eventDTO);
    }

    @PostMapping("/delete")
    public void deleteEvent(@RequestBody String eventId){
        eventService.deleteEventById(eventId);
    }

    @PutMapping("/update")
    public void updateEvent(@RequestBody UpdateEventDTO updateEventDTO){
        eventService.updateEvent(updateEventDTO);
    }

    @PutMapping("/updatenotif")
    public void updateEventNotif(@RequestBody EventDTO EventDTO){
        eventService.updateEventNotif(EventDTO);
    }

    @GetMapping(value = "/public_and_friends")
    public List<Event> getAllPublic(@RequestParam String customerId) {
        return eventService.getAllPublicAndFriendsEvents(customerId);
    }

    @GetMapping("/my{custId}")
    public ResponseEntity<List<EventDTO>> getEventsByCustId(@PathVariable String custId){
        List<EventDTO> eventsByCustId = eventService.getEventsByCustId(custId);
        return new ResponseEntity<>(eventsByCustId, HttpStatus.OK);
    }

    @GetMapping("/my/sorted{custId}")
    public ResponseEntity<List<EventDTO>> getEventsByCustIdSorted(@PathVariable String custId){
        List<EventDTO> eventsByCustId = eventService.getEventsByCustIdSorted(custId);
        return new ResponseEntity<>(eventsByCustId, HttpStatus.OK);
    }

    @GetMapping("/my/sorted/type{custId}")
    public ResponseEntity<List<EventDTO>> getEventsByCustIdSortedByType(@PathVariable String custId){
        List<EventDTO> eventsByCustId = eventService.getEventsByCustIdSortedByType(custId);
        return new ResponseEntity<>(eventsByCustId, HttpStatus.OK);
    }

    @GetMapping("/my/filter/{type}/{custId}")
    public ResponseEntity<List<EventDTO>> getEventsByCustIdFilterByType(@PathVariable("type") String type,
                                                                     @PathVariable("custId") String custId){
        List<EventDTO> eventsByCustId = eventService.getEventsByCustIdFilterByType(custId,type);
        return new ResponseEntity<>(eventsByCustId, HttpStatus.OK);
    }

    @GetMapping("/drafts{custId}")
    public ResponseEntity<List<Event>> getDraftsByCustId(@PathVariable String custId){
        List<Event> draftsByCustId = eventService.getDraftsByCustId(custId);
        return new ResponseEntity<>(draftsByCustId, HttpStatus.OK);
    }

    @GetMapping("/notes{custId}")
    public ResponseEntity<List<Event>> getNotesByCustId(@PathVariable String custId){
        List<Event> notesByCustId = eventService.getNotesByCustId(custId);
        return new ResponseEntity<>(notesByCustId, HttpStatus.OK);
    }

    @GetMapping("/invites{custId}")
    public ResponseEntity<List<Event>> getInvitesByCustId(@PathVariable String custId){
        List<Event> invitesByCustId = eventService.getInvitesByCustId(custId);
        return new ResponseEntity<>(invitesByCustId, HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}/{custId}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("eventId") String eventId,
                                                 @PathVariable("custId") String custId){
        EventDTO eventById = eventService.getEventById(eventId,custId);
        return new ResponseEntity<>(eventById, HttpStatus.OK);
    }

    @GetMapping("/note/{noteId}/{custId}")
    public ResponseEntity<EventDTO> getNoteById(@PathVariable("noteId") String noteId,
                                                @PathVariable("custId") String custId){
        EventDTO eventById = eventService.getNoteById(noteId,custId);
        return new ResponseEntity<>(eventById, HttpStatus.OK);
    }

    @GetMapping("/isParticipant")
    public ResponseEntity isParticipant(@RequestParam String customerId,@RequestParam String eventId) {
        if(eventService.isParticipant(customerId, eventId)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/addParticipant")
    public ResponseEntity addParticipant(@RequestParam String customerId,@RequestParam String eventId) {
        eventService.addParticipant(customerId, eventId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/removeParticipant")
    public ResponseEntity removeParticipant(@RequestParam String customerId,@RequestParam String eventId) {
        eventService.removeParticipant(customerId, eventId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getInviteNotifications")
    public ResponseEntity<List<InviteNotificationDTO>> getInviteNotifications(@RequestParam String customerId) {
        List<InviteNotificationDTO> notifications = eventService.getInviteNotifications(customerId);
        notifications.forEach(System.out::println);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/getNationalEvents")
    public ResponseEntity<List<Event>> getNationalEvents(@RequestParam String calendarId){
        List<Event> list = new LinkedList<>();
        try{
            list = eventService.getNationalEvents(calendarId, LocalDateTime.now(),LocalDateTime.now().plusYears(1));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(list, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<EventDTO>> getTimeline(@RequestParam String  login) {
        List<Event> list = eventService.getTimeline(login, LocalDateTime.now(),LocalDateTime.now().plusMonths(1));
        List<EventDTO> dtoList = new ArrayList<>();
        list.forEach((event) -> {
            EventDTO dto = new EventDTO();
            dto.setEvent(event);
            dtoList.add(dto);
        });

        return new ResponseEntity<>(dtoList,HttpStatus.OK);
    }

    @GetMapping ("/updatePriority")
    public ResponseEntity updatePriority(@RequestParam String customerId,@RequestParam String eventId, @RequestParam String priority) {
        eventService.updatePriority(customerId,eventId,priority);
        return new ResponseEntity(HttpStatus.OK);
    }
}
