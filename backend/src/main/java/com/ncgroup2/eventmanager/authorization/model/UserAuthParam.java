package com.ncgroup2.eventmanager.authorization.model;

public class UserAuthParam {
    String login;
    String password;

    public UserAuthParam() {
    }

    public UserAuthParam(String login, String password) {

        this.login = login;
        this.password = password;
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
