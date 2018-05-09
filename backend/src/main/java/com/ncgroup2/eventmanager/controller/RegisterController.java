package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.CustomerService;
import com.ncgroup2.eventmanager.service.sender.MyMailSender;
import com.ncgroup2.eventmanager.service.sender.SubjectEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final MyMailSender mailMyMailSender;

    @Autowired
    public RegisterController(CustomerService customerService, PasswordEncoder passwordEncoder, MyMailSender mailMyMailSender) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.mailMyMailSender = mailMyMailSender;
    }



    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addUser(@RequestBody Customer customer) {
        if (customerService.isCustomerPresent(customer.getLogin())) {
            return new ResponseEntity("This login is already taken", HttpStatus.BAD_REQUEST);
        }

        if (!customerService.isEmailUnique(customer.getEmail())) {
            return new ResponseEntity("This email is already taken", HttpStatus.BAD_REQUEST);
        }

        String token = UUID.randomUUID().toString();
        customer.setToken(token);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        customerService.register(customer);

        mailMyMailSender.sendEmail(customer.getEmail(), SubjectEnum.REGISTRATION, token);

        return new ResponseEntity( HttpStatus.OK);
    }

//    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    @ResponseStatus
    @GetMapping(value = "/registrationConfirm")
    public ResponseEntity confirmRegistration
            (@RequestParam String token) {
        Customer customer = customerService.getCustomerByToken(token);
        if (customer == null) {
            return new ResponseEntity("Your token is invalid", HttpStatus.BAD_REQUEST);
        }
        Instant expireDate = customer.getRegistrationDate().plus(24, ChronoUnit.HOURS);

        if (Instant.now().isAfter(expireDate)) {
            customerService.deleteCustomer(customer);
            return new ResponseEntity("Your token is expired. Please, register again",HttpStatus.BAD_REQUEST);
        }

        customerService.confirmCustomer(customer);


        return new ResponseEntity(HttpStatus.OK);
    }


}
