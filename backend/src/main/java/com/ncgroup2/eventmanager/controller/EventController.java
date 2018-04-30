package com.ncgroup2.eventmanager.controller;

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
    public void create(@RequestBody Event event){
        eventService.createEvent(event);
    }

    @GetMapping(value = "/public_and_friends")
    public List<Event> getAllPublic(@RequestParam String customerId) {
        return eventService.getAllPublicAndFriendsEvents(customerId);
    }

    @GetMapping("/{custId}")
        public List<Event> getEventsByCustId(@PathVariable String custId){
        return eventService.getEventsByCustId(custId);
    }

    @GetMapping()
    public Event getEventsById(@RequestParam String eventId){
        return eventService.getEventById(eventId);
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

