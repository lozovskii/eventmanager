package com.ncgroup2.eventmanager.beans;


import java.util.Random;

public class Customer {

    private long id;
//    private String email;
    private String login;
    private String password;

   public Customer() {
        Random random = new Random();
        id = random.nextInt(1000);
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

}
