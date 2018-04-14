package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.dao.CustomerDAO;
import com.ncgroup2.eventmanager.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.ncgroup2.eventmanager.beans.Customer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
@Controller
public class RegisterControler {
    @Autowired
    CustomerService customerService;



    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegister(Model model) {

        model.addAttribute("customer", new Customer());
        return "register";
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,Model model) {
        if(customerService.isCustomerPresent(customer.getLogin())) {
            model.addAttribute("exist",true);
            return "register";
        }
        customerService.register(customer);
        return "registration_complete";
    }
}
