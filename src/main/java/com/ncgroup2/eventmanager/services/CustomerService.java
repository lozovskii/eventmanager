package com.ncgroup2.eventmanager.services;

import com.ncgroup2.eventmanager.beans.Customer;
import com.ncgroup2.eventmanager.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {
    @Autowired
    CustomerDAO customerDAO;

    public void register(Customer customer) {
        customerDAO.insert(customer);
    }

    public boolean isCustomerPresent(String login) {
        return customerDAO.getByLogin(login)!=null;
    }
}
