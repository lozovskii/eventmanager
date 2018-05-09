package com.ncgroup2.eventmanager.authorization.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthentificationBox implements Authentication {

    private UserDetails userDetails;
    private boolean authenticated = true;

    @Autowired
    public AuthentificationBox(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userDetails.getPassword();
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated=isAuthenticated;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }
}
