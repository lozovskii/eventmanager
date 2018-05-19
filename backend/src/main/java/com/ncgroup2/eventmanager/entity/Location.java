package com.ncgroup2.eventmanager.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Location extends Entity {

    private String id;
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
                getCountry(),
                getCity(),
                getStreet(),
                getHouse(),
                getId()
        };
    }
}
