package com.ncgroup2.eventmanager.service.entityservice;

import com.ncgroup2.eventmanager.dao.CustomerDao;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerDao customerDao;

    @Override
    public void register(Customer customer) {
        customerDao.create(customer);
    }

    @Override
    public boolean isCustomerPresent(String login) {
        return customerDao.getEntityByField("login",login) != null;
    }

    @Override
    public void createVerificationToken(Customer customer, String token) {
        customerDao.updateField(customer, "token",  token);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return customerDao.getEntitiesByField("email",email) == null;
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customerDao.delete(customer);
    }

    @Override
    public Customer getCustomer(String token) {
        return customerDao.getEntityByField("token", token);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerDao.getEntityByField("email", email);
    }

    @Override
    @Transactional
    public void confirmCustomer(Customer customer) {
        customer.setVerified(true);
        customer.setToken("");

        customerDao.update(customer);
    }

    @Override
    @Transactional
    public void updatePassword(Customer customer) {
        customer.setToken("");

        customerDao.update(customer);
    }

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
    public List<Relationship> getNotifications(String login) {
        return customerDao.getNotifications(login);
    }

    @Override
    public void addFriend(String login) {
        customerDao.addFriend(login);
    }

    @Override
    public void acceptFriend(String token) {
        customerDao.acceptFriend(token);
    }

    @Override
    public void rejectFriend(String token) {
        customerDao.rejectFriend(token);
    }

    @Override
    public void uploadAvatar(Customer customer) {
        customerDao.uploadAvatar(customer);
    }
}