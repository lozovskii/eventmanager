package com.ncgroup2.eventmanager.dto;

import com.ncgroup2.eventmanager.entity.Event;

public class EventDTO {

    private Event event;
    private AdditionalEventModelDTO additionEvent;

    public EventDTO() {
    }

    public EventDTO(Event event, AdditionalEventModelDTO additionEvent) {
        this.event = event;
        this.additionEvent = additionEvent;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public AdditionalEventModelDTO getAdditionEvent() {
        return additionEvent;
    }

    public void setAdditionEvent(AdditionalEventModelDTO additionEvent) {
        this.additionEvent = additionEvent;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "event=" + event +
                ", additionEvent=" + additionEvent +
                '}';
    }
}
