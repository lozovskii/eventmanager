package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerDao customerDao;

    @Override
    public Customer getByLogin(String login) {
        return customerDao.getByLogin(login);
    }

    @Override
    public void edit(Customer customer) {
        customerDao.edit(customer);
    }
}