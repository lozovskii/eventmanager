package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import com.ncgroup2.eventmanager.service.sender.Sender;
import com.ncgroup2.eventmanager.service.sender.SubjectEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final Sender mailSender;

    @Autowired
    public RegisterController(CustomerService customerService, PasswordEncoder passwordEncoder, Sender mailSender) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }



    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addUser(@RequestBody Customer customer) {
        if (customerService.isCustomerPresent(customer.getLogin())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This login is already taken");
        }

        if (!customerService.isEmailUnique(customer.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email is already taken");
        }

        String token = UUID.randomUUID().toString();
        customer.setToken(token);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        customerService.register(customer);

        mailSender.sendEmail(customer.getEmail(), SubjectEnum.REGISTRATION, token);

        return ResponseEntity.status(HttpStatus.OK).body("Registration completed");
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {
        Customer customer = customerService.getCustomer(token);
        if (customer == null) {
            model.addAttribute("message", "Invalid token");
            return "/registration/badUser";
        }
        Instant expireDate = customer.getRegistrationDate().plus(24, ChronoUnit.HOURS);

        if (Instant.now().isAfter(expireDate)) {
            model.addAttribute("message", "This link is no longer valid. Please, register again");
            customerService.deleteCustomer(customer);
            return "/registration/badUser";
        }

        customerService.confirmCustomer(customer);


        return "registration/successful_confirmation";
    }


}
