package com.ncgroup2.eventmanager.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Friends {
    private String current;
    private String another;
    private boolean isfriends;
    private boolean isrequest;
}
