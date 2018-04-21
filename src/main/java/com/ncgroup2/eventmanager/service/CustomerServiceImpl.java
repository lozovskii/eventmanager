package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<Customer> search(String search) {
        return customerDao.search(search);
    }

    @Override
    public List<Customer> getFriends(String login) {
        return customerDao.getFriends(login);
    }

    @Override
    public void delete(String login) {
        customerDao.delete(login);
    }
}