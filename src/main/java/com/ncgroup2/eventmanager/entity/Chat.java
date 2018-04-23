package com.ncgroup2.eventmanager.entity;

public class Chat extends Entity {

    private String eventId;
    private boolean withOwner;

    public Chat() {
    }

    public Chat(String eventId, boolean withOwner) {
        this.eventId = eventId;
        this.withOwner = withOwner;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public boolean isWithOwner() {
        return withOwner;
    }

    public void setWithOwner(boolean withOwner) {
        this.withOwner = withOwner;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "eventId=" + eventId +
                ", withOwner=" + withOwner +
                '}';
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.getName(),
                this.getEventId(),
                this.isWithOwner()
        };
    }
}
