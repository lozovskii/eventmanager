package com.ncgroup2.eventmanager.entity;

import java.util.UUID;

public abstract class Entity {

    protected String id;

    protected String name;

    Entity(){

        UUID uuid = UUID.randomUUID();

        this.id = uuid.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract public Object[] getParams();
}
