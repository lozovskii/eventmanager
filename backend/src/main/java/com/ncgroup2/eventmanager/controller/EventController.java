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

//    @PostMapping
//    public void create(@RequestParam("event") Event event,
//                       @RequestParam("startDate") Long frequencyNumber,
//                       String frequencyPeriod,
//                       List<String> people){
//        EventDTO eventDto = new EventDTO();
//        eventService.createEvent(event, eventDto);
//    }

    @PostMapping("/delete")
    public void deleteEvent(@RequestBody String eventId){
        eventService.deleteEvent(eventId);
    }

    @GetMapping("/{custId}")
        public ResponseEntity<List<Event>> getEventsByCustId(@PathVariable String custId){
        List<Event> eventsByCustId = eventService.getEventsByCustId(custId);
        return new ResponseEntity<>(eventsByCustId, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Event> getEventsById(@RequestParam String eventId){
        Event eventById = eventService.getEventById(eventId);
        return new ResponseEntity<>(eventById, HttpStatus.OK);
    }
}

