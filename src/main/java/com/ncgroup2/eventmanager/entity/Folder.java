package com.ncgroup2.eventmanager.entity;

public class Folder extends Entity {

    String customer_id;

    String title;

    Event[] notes;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Event[] getNotes() {
        return notes;
    }

    public void setNotes(Event[] notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "customer_id='" + customer_id + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
