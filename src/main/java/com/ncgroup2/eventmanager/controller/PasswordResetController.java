package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.event.OnPasswordResetEvent;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class PasswordResetController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String showReset(Model model) {

        model.addAttribute("customer", new Customer());
        return "/reset";

    }


    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String resetPassword(

            @Valid @ModelAttribute("customer") Customer customer,
            Model model,
            @RequestParam("email") String userEmail,
            WebRequest request) {

        if(customerService.isEmailUnique(userEmail)) {

            model.addAttribute("customer_not_found", true);
            return "/reset";

        }else {

            String appUrl = request.getContextPath();

            eventPublisher.publishEvent(new OnPasswordResetEvent
                    (customer, request.getLocale(), appUrl));

            return "/reset_complete";

        }
}

}



