package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.event.OnRegistrationCompleteEvent;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Controller
public class RegisterController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute("customer") Customer customer,
                          BindingResult result, Model model, WebRequest request) {

        if (customerService.isCustomerPresent(customer.getLogin())) {
            model.addAttribute("customer_exist", true);
            return "redirect:/?q=customer_exist";
        }

        if (!customerService.isEmailUnique(customer.getEmail())) {
            model.addAttribute("email_exist", true);
            return "redirect:/?q=email_exist";
        }

        if (customer.getPassword().trim().isEmpty()) {
            model.addAttribute("empty_password", true);
            return "redirect:/?q=empty_password";
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer registered = customerService.register(customer);

        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));

        return "redirect:/?q=link_sent";
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {
        Customer customer = customerService.getCustomer(token);
        if (customer == null) {
            model.addAttribute("message", "Invalid token");
            return "redirect:/?q=invalid_token";
        }

        Instant expireDate = customer.getRegistrationDate().plus(24, ChronoUnit.HOURS);

        if (Instant.now().isAfter(expireDate)) {
            model.addAttribute("message", "This link is no longer valid. Please, register again");
            customerService.deleteCustomer(customer);
            return "redirect:/?q=date_expired";
        }

        customerService.confirmCustomer(customer);

        return "redirect:/?q=successful_confirmation";
    }
}