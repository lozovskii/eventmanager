package com.ncgroup2.eventmanager.dto;

import java.util.List;

public class AdditionalEventModelDTO {

    private List<String> people;
    private Long frequencyNumber;
    private String frequencyPeriod;
    private String priority;

    AdditionalEventModelDTO(){
    }

    public AdditionalEventModelDTO(List<String> people, Long frequencyNumber, String frequencyPeriod, String priority) {
        this.people = people;
        this.frequencyNumber = frequencyNumber;
        this.frequencyPeriod = frequencyPeriod;
        this.priority = priority;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "AdditionalEventModelDTO{" +
                "people=" + people +
                ", frequencyNumber=" + frequencyNumber +
                ", frequencyPeriod='" + frequencyPeriod + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
