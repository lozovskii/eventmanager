package com.ncgroup2.eventmanager.dto;

import lombok.Data;

@Data
public class EventPriorityDTO {

    private String eventId;
    private String priority;

    public EventPriorityDTO() {
    }

    public EventPriorityDTO(String eventId, String priority) {
        this.eventId = eventId;
        this.priority = priority;
    }
}
