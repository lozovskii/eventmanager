package com.ncgroup2.eventmanager.dto;

import com.ncgroup2.eventmanager.entity.Event;
import lombok.Data;

@Data
public class EventDTO {

    private Event event;
    private AdditionalEventModelDTO additionEvent;

    public EventDTO() {
    }

    public EventDTO(Event event, AdditionalEventModelDTO additionEvent) {
        this.event = event;
        this.additionEvent = additionEvent;
    }
}
