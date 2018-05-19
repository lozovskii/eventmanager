package com.ncgroup2.eventmanager.dto;

import com.ncgroup2.eventmanager.entity.Message;

public class MessageDTO {
    private Message message;
    public MessageDTO(){}

    public Message getMessage(){return message;}
    public void setMessage(Message message){this.message = message;}
}
