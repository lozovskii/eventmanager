package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Customer;

import java.util.List;

public interface CustomerDao {
    Customer getByLogin(String login);
    void edit(Customer customer);
    List<Customer> search(String search);
    List<Customer> getFriends(String login);
    void delete(String login);
}