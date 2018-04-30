package com.ncgroup2.eventmanager.dto;

import java.awt.*;
import java.util.List;

public class EventDTO {
    private Long frequencyNumber;
    private String frequencyPeriod;
    private List<String> people;
    private Event event;

    public EventDTO() {
    }

    public EventDTO(Event event, Long frequencyNumber, String frequencyPeriod, List<String> people) {
        this.event = event;
        this.frequencyNumber = frequencyNumber;
        this.frequencyPeriod = frequencyPeriod;
        this.people = people;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getFrequencyNumber() {
        return frequencyNumber;
    }

    public void setFrequencyNumber(Long frequencyNumber) {
        this.frequencyNumber = frequencyNumber;
    }

    public String getFrequencyPeriod() {
        return frequencyPeriod;
    }

    public void setFrequencyPeriod(String frequencyPeriod) {
        this.frequencyPeriod = frequencyPeriod;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }
}
