package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Relationship;

import java.util.Collection;
import java.util.List;

public interface CustomerDao extends DAO<Customer,Object> {

//    void create(Customer customer);

//    void delete(Customer customer);

//    Customer getEntityByField(String fieldName, String fieldValue);

//    Collection<Customer> getEntitiesByField(String fieldName, String fieldValue);

//    Collection<Customer> getEntitiesByField();

//    void update(Customer customer);

//    void updateField(Customer customer, String fieldName, Object fieldValue);

    // PROFILE METHODS
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
    void deleteUnverifiedCustomers();
}
