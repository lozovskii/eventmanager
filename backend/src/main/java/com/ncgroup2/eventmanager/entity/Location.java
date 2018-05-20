package com.ncgroup2.eventmanager.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Location extends Entity {

    private String id;
    private String event_id;
    private String country;
    private String city;
    private String street;
    private String house;

    public Location() {
        UUID uuid = UUID.randomUUID();

        this.id = uuid.toString();
    }

    public Object[] getParams() {
        return new Object[] {
                getId(),
                getEvent_id(),
                getCountry(),
                getCity(),
                getStreet(),
                getHouse()
        };
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", event_id='" + event_id + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                '}';
    }
}
