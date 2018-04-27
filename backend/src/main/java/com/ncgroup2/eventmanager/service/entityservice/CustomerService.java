package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Customer;

public interface CustomerService {

    void register(Customer customer);
    boolean isCustomerPresent(String login);
    void createVerificationToken(Customer customer, String token);
    boolean isEmailUnique(String email);
    void deleteCustomer(Customer customer);
    Customer getCustomer(String token);
    Customer getCustomerByEmail(String email);
    void confirmCustomer(Customer customer);
    void updatePassword(Customer customer);

    Customer getByLogin(String login);
    void edit(Customer customer);
    List<Customer> search(String search);
    List<Customer> getFriends(String login);
    void delete(String login);
    List<Relationship> getNotifications(String login);
    void addFriend(String login);
    void acceptFriend(String token);
    void rejectFriend(String token);
    void uploadAvatar(Customer customer);
}
