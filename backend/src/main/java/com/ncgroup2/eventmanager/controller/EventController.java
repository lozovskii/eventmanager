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
        System.out.println(event.toString());
        eventService.createEvent(event);
    }

    @GetMapping
    public List<Event> getEvents(){
        List<Event> events = eventService.getAllEvents();
        System.out.println(events);
        return events;
    }

    @GetMapping("/{custId}")
        public List<Event> getEventsByCustId(@PathVariable String custId){
        System.out.println("Controller works!");
        List<Event> events = eventService.getEventsByCustId(custId);
        System.out.println(events);
        return events;
    }
}

