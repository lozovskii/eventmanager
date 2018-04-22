package com.ncgroup2.eventmanager.entity;

public class Notification {

    private String status;
    private String sender;
    private String recipient;

    public Notification() {
    }

    public Notification(String status, String sender, String recipient) {
        this.status = status;
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "status='" + status + '\'' +
                '}';
    }
}