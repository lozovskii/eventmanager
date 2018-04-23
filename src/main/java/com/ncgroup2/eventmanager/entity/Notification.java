package com.ncgroup2.eventmanager.entity;

public class Notification {

    private String id;
//    private String status;
//    private String sender;
//    private String recipient;
//    private String token;
//    private String request;
    private String name;
    private String second_name;

//    public Notification() {
//    }
//
//    public Notification(String status, String sender, String recipient, String token, String request) {
//        this.status = status;
//        this.sender = sender;
//        this.recipient = recipient;
//        this.token = token;
//        this.request = request;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getSender() {
//        return sender;
//    }
//
//    public void setSender(String sender) {
//        this.sender = sender;
//    }
//
//    public String getRecipient() {
//        return recipient;
//    }
//
//    public void setRecipient(String recipient) {
//        this.recipient = recipient;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public String getRequest() {
//        return " wants add you to friends";
//    }
//
//    public void setRequest(String request) {
//        this.request = request;
//    }
//
//    @Override
//    public String toString() {
//        return "Notification{" +
//                "status='" + status + '\'' +
//                '}';
//    }
}