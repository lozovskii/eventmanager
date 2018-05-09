package com.ncgroup2.eventmanager.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Arrays;

@Getter
@Setter
public class Customer extends Entity {

    private String email;
    private String login;
    private String password;
    private String secondName;
    private String phone;
    private boolean isVerified;
    private Instant registrationDate;
    private String token;
    private String avatar;

    public Customer() {

        isVerified =false;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + getName() + '\'' +
                ", secondName='" + secondName + '\'' +
                ", phone='" + phone + '\'' +
                ", isVerified=" + isVerified +
                ", registrationDate=" + registrationDate +
                ", token='" + token + '\'' +
                ", image=" + avatar +
                '}';
    }


    // Params for UPDATE. Don't include registrationDate (not update)
    @Override
    public Object[] getParams() {
        return new Object[]{
                this.getName(),
                this.getSecondName(),
                this.getPhone(),
                this.getLogin(),
                this.getEmail(),
                this.getPassword(),
                this.isVerified(),
                this.getToken(),
                this.getAvatar(),
                this.getId()
        };
    }
}
