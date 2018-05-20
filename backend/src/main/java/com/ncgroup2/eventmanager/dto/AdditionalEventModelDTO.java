package com.ncgroup2.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ncgroup2.eventmanager.entity.Location;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdditionalEventModelDTO {

    private List<String> people;
    private Long frequencyNumber;
    private String frequencyPeriod;
    private String priority;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTimeNotification;
    private Location location;

    AdditionalEventModelDTO(){
    }

    public AdditionalEventModelDTO(List<String> people, Long frequencyNumber, String frequencyPeriod, String priority,
                                   LocalDateTime startTimeNotification, Location location) {
        this.people = people;
        this.frequencyNumber = frequencyNumber;
        this.frequencyPeriod = frequencyPeriod;
        this.priority = priority;
        this.startTimeNotification = startTimeNotification;
        this.location = location;
    }

    @Override
    public String toString() {
        return "AdditionalEventModelDTO{" +
                "people=" + people +
                ", frequencyNumber=" + frequencyNumber +
                ", frequencyPeriod='" + frequencyPeriod + '\'' +
                ", priority='" + priority + '\'' +
                ", startTimeNotification=" + startTimeNotification +
                ", location=" + location +
                '}';
    }
}
