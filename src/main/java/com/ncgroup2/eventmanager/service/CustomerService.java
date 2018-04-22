package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Notification;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CustomerService {
    Customer getByLogin(String login);
    void edit(Customer customer);
    List<Customer> search(String search);
    List<Customer> getFriends(String login);
    void delete(String login);
    Map<Notification, String> getNotifications(String login);
    void addFriend(String login);
    void acceptFriend(String uuid);
    void rejectFriend(String uuid);
}
