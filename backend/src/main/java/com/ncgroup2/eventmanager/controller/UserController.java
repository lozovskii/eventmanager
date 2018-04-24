package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private CustomerService customerService;

    @Autowired
    public UserController(CustomerService customerService) {
        this.customerService = customerService;
    }

//    @PostMapping
//    public ResponseEntity<Customer> register(@RequestBody Customer customer){
//        System.out.println(customer.toString());
//        Customer registeredUser = customerService.register(customer);
//        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity getCustomer (@RequestParam String login) {
        return new ResponseEntity(customerService.findByLogin(login), HttpStatus.OK);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
