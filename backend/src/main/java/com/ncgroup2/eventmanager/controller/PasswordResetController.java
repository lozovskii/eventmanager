package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.sender.Sender;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import com.ncgroup2.eventmanager.service.sender.SubjectEnum;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Sender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String resetPassword(

            Model model,
            @RequestParam("reset_email") String reset_email
            ) {

        System.out.println(reset_email);

        if(customerService.isEmailUnique(reset_email)) {

            model.addAttribute("customer_not_found", true);
            return "redirect:/?q=customer_not_found";

        }else {

            Customer customer = customerService.getCustomerByEmail(reset_email);

            String token = UUID.randomUUID().toString();

            customerService.createVerificationToken(customer, token);

            mailSender.sendEmail(customer.getEmail(), SubjectEnum.RESET_PASSWORD, token);

        } return "redirect:/?q=We_sent_confirmation_email";
    }

        @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
        public String displayResetPasswordPage(
                Model model,
                @RequestParam ("token") String token) {

            Customer customer = customerService.getCustomerByToken(token);

            if (customer == null) {

                model.addAttribute("customer_not_found", true);

                return "redirect:/?q=customer_not_found";
            }

            model.addAttribute("token", token);

            return "reset/reset_password";

        }

    @RequestMapping(value = "/setNewPassword", method = RequestMethod.POST)
    public String setPassword(
            Model model,
            @RequestParam ("password") String password,
            @RequestParam ("token") String token)
    {

        Customer customer = customerService.getCustomerByToken(token);

        System.out.println(token);

        System.out.println(customer.getEmail());

        if (customer == null) {

            model.addAttribute("customer_not_found", true);

            return "redirect:/?q=customer_not_found";

        } else{


            customer.setPassword(passwordEncoder.encode(password));

            customerService.updatePassword(customer);

        }


        return "redirect:/";

    }

}



