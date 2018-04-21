package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer getByLogin(String login);
    void edit(Customer customer);
    List<Customer> search(String search);
    List<Customer> getFriends(String login);
    void delete(String login);
}
