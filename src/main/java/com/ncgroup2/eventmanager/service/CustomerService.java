package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Customer;

public interface CustomerService {
    Customer getByLogin(String login);
    void edit(Customer customer);
}
