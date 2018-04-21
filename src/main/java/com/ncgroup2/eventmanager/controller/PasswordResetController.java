package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String showReset(Model model) {

        return "redirect:/index.html";

    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String resetPassword(

            Model model,
            @ModelAttribute("email")
            @RequestParam("email") String userEmail,
            HttpServletRequest request) {

        if(customerService.isEmailUnique(userEmail)) {

            model.addAttribute("customer_not_found", true);
            return "redirect:/index.html?not_found";

        }else {

            Customer customer = customerService.getCustomerByEmail(userEmail);

            String token = UUID.randomUUID().toString();

            customerService.createVerificationToken(customer, token);

            String recipientAddress = customer.getEmail();

            String subject = "Password reset";

            String confirmationUrl
                    = "/resetPassword?token=" + token;

            String message = "Confirmation link: \n";

            SimpleMailMessage email = new SimpleMailMessage();

            email.setTo(recipientAddress);

            email.setSubject(subject);

            email.setText(message  + "https://rocky-dusk-73382.herokuapp.com" + confirmationUrl);

            mailSender.send(email);

            model.addAttribute("link_sent",true);

            return "redirect:/index.html";

        }
    }

        @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
        public String displayResetPasswordPage(
                Model model,
                @RequestParam ("token") String token) {

            Customer customer = customerService.getCustomer(token);

            if (customer == null) {

                model.addAttribute("customer_not_found", true);

                return "redirect:/index.html?not_found";
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

        Customer customer = customerService.getCustomer(token);

        System.out.println(token);

        System.out.println(customer.getEmail());

        if (customer == null) {

            model.addAttribute("customer_not_found", true);

            return "redirect:/index.html?not_found";

        } else{


            customer.setPassword(passwordEncoder.encode(password));

            customerService.updatePassword(customer);

        }


        return "redirect:/index.html";

    }

}



