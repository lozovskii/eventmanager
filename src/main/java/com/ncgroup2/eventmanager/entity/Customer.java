package com.ncgroup2.eventmanager.entity;

import java.time.Instant;

public class Customer {

    private String id;
    private String name;
    private String secondName;
    private String email;
    private String login;
    private String password;
    private String phone;
    private boolean isVerified;
    private Instant registrationDate;
    private String token;
    private byte[] avatar;

    public Customer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", phone='" + phone + '\'' +
                ", isVerified=" + isVerified +
                ", registrationDate=" + registrationDate +
                ", token='" + token + '\'' +
                '}';
    }

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