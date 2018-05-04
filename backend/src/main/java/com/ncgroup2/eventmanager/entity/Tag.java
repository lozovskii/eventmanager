package com.ncgroup2.eventmanager.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tag extends Entity {

    private int count;

    @Override
    public String toString() {
        return super.toString() +
                "\nTag{" +
                "count=" + count +
                "} ";
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                getId(),
                getName(),
                getCount()
        };
    }
}
