package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    CustomerService customerService;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String reserPassword(@RequestParam("reset_email") String reset_email, Model model) {
        if (customerService.isEmailUnique(reset_email)) {
            model.addAttribute("customer_not_found", true);

            return "redirect:/?=customer_not_found";
        } else {
            Customer customer = customerService.getCustomerByEmail(reset_email);
            String token = UUID.randomUUID().toString();
            customerService.createVerificationToken(customer, token);
            String recipientAddress = customer.getEmail();
            String subject = "Password reset";
            String confirmationUrl = "/resetPassword?token=" + token;
            String message = "Confirmation link: \n";
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message  + "https://rocky-dusk-73382.herokuapp.com" + confirmationUrl);
            mailSender.send(email);

            return "redirect:/?q=We_sent_confirmation_email";
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public String displayResetPasswordPage(@RequestParam ("token") String token, Model model) {
        Customer customer = customerService.getCustomer(token);

        if (customer == null) {
            model.addAttribute("customer_not_found", true);
            return "redirect:/?q=customer_not_found";
        }

        model.addAttribute("token", token);
        return "reset/reset_password";
    }

    @RequestMapping(value = "/setNewPassword", method = RequestMethod.POST)
    public String setPassword(@RequestParam ("password") String password, @RequestParam ("token") String token,
                              Model model) {
        Customer customer = customerService.getCustomer(token);

        if (customer == null) {
            model.addAttribute("customer_not_found", true);
            return "redirect:/?q=customer_not_found";
        } else {
            customer.setPassword(passwordEncoder.encode(password));
            customerService.updatePassword(customer);
        }

        return "redirect:/";
    }
}