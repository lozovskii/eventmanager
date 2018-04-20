package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Customer;

public interface CustomerService {

    Customer register(Customer customer);
    boolean isCustomerPresent(String login);
    void createVerificationToken(Customer customer, String token);
    boolean isEmailUnique(String email);
    void deleteCustomer(Customer customer);
    Customer getCustomer(String token);
    void confirmCustomer(Customer customer);

}
