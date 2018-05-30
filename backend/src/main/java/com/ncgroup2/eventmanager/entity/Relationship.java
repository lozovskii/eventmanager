package com.ncgroup2.eventmanager.entity;

public class Relationship extends Entity{

    private String second_name;
    private String login;

    @Override
    public Object[] getParams() {
        return new Object[]{
                getName(),
                getSecond_name(),
                getLogin(),
                getId()
        };
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}