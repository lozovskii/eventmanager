package com.ncgroup2.eventmanager.entity;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class Entity {

    protected String id;

    protected String name;

    Entity(){

        UUID uuid = UUID.randomUUID();

        this.id = uuid.toString();
    }

    abstract public Object[] getParams();
}
