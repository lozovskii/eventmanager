package com.ncgroup2.eventmanager.dao;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Friends;
import com.ncgroup2.eventmanager.entity.Page;
import com.ncgroup2.eventmanager.entity.Relationship;

import java.util.List;

public interface CustomerDao extends DAO<Customer, Object> {

    Customer getByLogin(String login);

    void edit(Customer customer);

    Page<Customer> search(int pageNo, int pageSize, String search);

    List<Customer> getFriends(String login);

    void delete(String login);

    List<Relationship> getNotifications(String login);

    void addFriend(String login);

    void cancelRequest(String login);

    void acceptFriend(String token);

    void rejectFriend(String token);

    void uploadAvatar(Customer customer);

    void deleteUnverifiedCustomers();

    boolean isCustomerExist(String login);

    void addGoogleId(String email, String id);

    boolean isFriends (String currentCustomerId, String customerId);

    List<Friends> getFriendOrRequest(String login);
}
