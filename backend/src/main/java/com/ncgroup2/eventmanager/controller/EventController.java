package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.dto.EventDTO;
import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void updateEvent(@RequestBody EventDTO eventDTO){
        System.out.println("controller works!");
        eventService.updateEvent(eventDTO);
    }

    @GetMapping(value = "/public_and_friends")
    public List<Event> getAllPublic(@RequestParam String customerId) {
        return eventService.getAllPublicAndFriendsEvents(customerId);
    }

    @GetMapping("/my{custId}")
    public ResponseEntity<List<Event>> getEventsByCustId(@PathVariable String custId){
        List<Event> eventsByCustId = eventService.getEventsByCustId(custId);
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

    @GetMapping("")
    public ResponseEntity<EventDTO> getEventsById(@RequestParam String eventId){
        EventDTO eventById = eventService.getEventById(eventId);
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
}
