package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.event.OnRegistrationCompleteEvent;
import com.ncgroup2.eventmanager.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private CustomerService customerService;
    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegisterController(CustomerService customerService, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addUser(@RequestBody Customer customer, BindingResult result, Model model, WebRequest request) {
        if (customerService.isCustomerPresent(customer.getLogin())) {
            model.addAttribute("customer_exist", true);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login");
        }

        if(!customerService.isEmailUnique(customer.getEmail())) {
            model.addAttribute("email_exist", true);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");
        }

        if(customer.getPassword().trim().isEmpty()) {
            model.addAttribute("empty_password",true);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password");
        }
        Customer registered;
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        try {
            registered = customerService.register(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or login is already taken");
        }
//        try {
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent
                (registered, request.getLocale(), appUrl));
//        } catch (Exception me) {
//            return "error";
//        }
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
