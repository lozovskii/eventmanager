package com.ncgroup2.eventmanager.dto;

import com.ncgroup2.eventmanager.entity.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateEventDTO {
    private Event event;
    private List<String> newPeople;
    private List<String> removedPeople;
    private Long frequencyNumber;
    private String frequencyPeriod;
    private String priority;
}
