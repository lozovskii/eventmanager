package com.ncgroup2.eventmanager.service.entityservice;

import com.ncgroup2.eventmanager.dao.impl.postgres.CustomerDaoImpl;
import com.ncgroup2.eventmanager.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {

    @Autowired
    CustomerDaoImpl customerDaoImpl;

    public void register(Customer customer) {

        customerDaoImpl.addCustomer(customer);
    }

    public boolean isCustomerPresent(String login) {

        return customerDaoImpl.getByField("login",login)!=null;
    }

    public void createVerificationToken(Customer customer, String token) {

        customerDaoImpl.updateField(customer, "token",  token);
    }

    public boolean isEmailUnique(String email) {

        return customerDaoImpl.getByField("email",email)==null;
    }


    public void deleteCustomer(Customer customer) {

        customerDaoImpl.deleteCustomer(customer);
    }

    public Customer getCustomer(String token) {

        return customerDaoImpl.getByField("token", token);
    }

    public Customer getCustomerByEmail(String email) {

        return customerDaoImpl.getByField("email", email);
    }

    @Transactional
    public void confirmCustomer(Customer customer) {

        customer.setVerified(true);
        customer.setToken("");

        customerDaoImpl.updateCustomer(customer);
    }

    @Transactional
    public void updatePassword(Customer customer) {

        customer.setToken("");

        customerDaoImpl.updateCustomer(customer);
    }

}