package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Event;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{custId}")
        public List<Event> getEventsByCustId(@PathVariable String custId){
        return eventService.getEventsByCustId(custId);
    }

    @GetMapping()
    public Event getEventsById(@RequestParam String eventId){
        return eventService.getEventById(eventId);
    }
}

