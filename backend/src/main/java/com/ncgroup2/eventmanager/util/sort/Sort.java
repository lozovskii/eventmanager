package com.ncgroup2.eventmanager.util.sort;

public class Sort {

    static final Direction DEFAULT_DIRECTION = Direction.ASC;
    String direction;
    String property;

    public Sort(String property){
        new Sort(DEFAULT_DIRECTION, property);
    }

    public Sort(Direction direction, String property){
        this.direction = direction.toString();
        this.property = property;
    }

    public String getOrder(){
        return " " + property + " " + direction;
    }
}
