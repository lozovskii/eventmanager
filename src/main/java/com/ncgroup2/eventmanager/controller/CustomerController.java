package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("profile")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(value = "{login}")
    public String getCustomerByLogin(@PathVariable("login") String login, Principal principal, Model model) {
        Customer customer = customerService.getByLogin(login);
        model.addAttribute("customer", customer);
        model.addAttribute("name", principal.getName());

        return "profile";
    }

    @RequestMapping(value = "{login}/edit", method = RequestMethod.GET)
    public String editGet(@PathVariable("login") String login, Model model) {
        Customer customer = customerService.getByLogin(login);
        model.addAttribute("customer", customer);

        return "edit";
    }

    @RequestMapping(value = "{login}/edit", method = RequestMethod.POST)
    public String editPost(@PathVariable("login") String login, @ModelAttribute("customer") Customer customer,
                           Model model) {
        model.addAttribute("name", customer.getFirstName());
        model.addAttribute("second_name", customer.getLastName());
        model.addAttribute("phone", customer.getPhone());
        customerService.edit(customer);

        return "redirect:/profile/{login}";
    }
}