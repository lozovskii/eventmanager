package com.ncgroup2.eventmanager.service;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Friends;
import com.ncgroup2.eventmanager.entity.Page;
import com.ncgroup2.eventmanager.entity.Relationship;

import java.util.List;

public interface CustomerService {

    void register(Customer customer);
    boolean isCustomerPresent(String login);
    void createVerificationToken(Customer customer, String token);
    boolean isEmailUnique(String email);
    void deleteCustomer(Customer customer);
    Customer getCustomerByToken(String token);
    Customer getCustomerByEmail(String email);
    void confirmCustomer(Customer customer);
    void updatePassword(Customer customer);
    Customer getByLogin(String login);
    void edit(Customer customer);
//    List<Customer> search(String search);
    Page<Customer> search(int pageNo, int pageSize, String search);
    List<Customer> getFriends(String login);
    void delete(String login);
    List<Relationship> getNotifications(String login);
    void addFriend(String login);
    void cancelRequest(String login);
    void acceptFriend(String token);
    void rejectFriend(String token);
    void uploadAvatar(Customer customer);

    Customer getById(String id);

    Customer findByLogin(String login);

    Customer getByGoogleId(String googleId);

    void addGoogleId(String email, String googleId);

    boolean isFriends (String currentCustomerId, String customerId);

    List<Friends> getFriendOrRequest(String login);
}
