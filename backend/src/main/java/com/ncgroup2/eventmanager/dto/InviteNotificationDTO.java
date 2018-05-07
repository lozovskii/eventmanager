package com.ncgroup2.eventmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteNotificationDTO {
    private String eventId;
    private String eventName;
    private String inviter;

    @Override
    public String toString() {
        return "InviteNotificationDTO{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", inviter='" + inviter + '\'' +
                '}';
    }
}
