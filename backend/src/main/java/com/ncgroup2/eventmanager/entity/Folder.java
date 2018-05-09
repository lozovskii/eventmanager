package com.ncgroup2.eventmanager.entity;

public class Folder extends Entity {

    String customer_id;

    String name;

    Event[] notes;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public Object[] getParams() {
        return new Object[]{
                this.getName(),
                this.getCustomer_id(),
                this.getId()
        };
    }
}
