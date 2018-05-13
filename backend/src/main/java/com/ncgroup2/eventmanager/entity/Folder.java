package com.ncgroup2.eventmanager.entity;

import lombok.Data;

@Data
public class Folder extends Entity {

    private String customerId;
    private boolean isshared;

    public Folder() {
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.getName(),
                this.getCustomerId(),
                this.isIsshared()
        };
    }
}
