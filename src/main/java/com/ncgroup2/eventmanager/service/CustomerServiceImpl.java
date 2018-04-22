package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Override
    public Map<Notification, String> getNotifications(String login) {
        return customerDao.getNotifications(login);
    }

    @Override
    public void addFriend(String login) {
        customerDao.addFriend(login);
    }

    @Override
    public void acceptFriend(String uuid) {
        customerDao.acceptFriend(uuid);
    }

    @Override
    public void rejectFriend(String uuid) {
        customerDao.rejectFriend(uuid);
    }
}