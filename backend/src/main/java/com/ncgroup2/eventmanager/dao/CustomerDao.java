package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Customer;

import java.util.Collection;

public interface CustomerDao {

    void addCustomer(Customer customer);

    void deleteCustomer(Customer customer);

    Customer getByField(String fieldName, String fieldValue);

    Collection<Customer> getCustomers(String fieldName, String fieldValue);

    Collection<Customer> getCustomers();

    void updateCustomer(Customer customer);

    void updateField(Customer customer, String fieldName, Object fieldValue);

}
