package com.ncgroup2.eventmanager.services;

import com.ncgroup2.eventmanager.beans.Customer;
import com.ncgroup2.eventmanager.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {
    @Autowired
    CustomerDAO customerDAO;

    public Customer register(Customer customer) {
        return customerDAO.insert(customer);
    }

    public boolean isCustomerPresent(String login) {
        return customerDAO.getByField("login",login)!=null;
    }

    public void createVerificationToken(Customer customer, String token) {
        customerDAO.updateField(customer, "token",  token);
    }

    public Customer getByToken (String token) {
        return customerDAO.getByField("token", token);
    }
@Transactional
    public void confirmCustomer(Customer customer) {
        customerDAO.updateField(customer,"isVerified", Boolean.TRUE);
        customerDAO.updateField(customer,"token","");
    }

    public void removeCustomer(Customer customer) {
        customerDAO.delete(customer);
    }

    public boolean isEmailUnique(String email) {
        return customerDAO.getByField("email",email)==null;
    }
}
