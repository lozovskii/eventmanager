package com.ncgroup2.eventmanager.authorization.service;

import com.ncgroup2.eventmanager.entity.Customer;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenGenerator {
    public String generateToken(Customer customer);

    public UserDetails getUserDetails (String jwt);
}
