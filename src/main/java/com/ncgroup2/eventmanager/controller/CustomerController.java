package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Notification;
import com.ncgroup2.eventmanager.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(value = "/{login}")
    public String getCustomerByLogin(@PathVariable("login") String login, Principal principal, Model model) {
        Customer customer = customerService.getByLogin(login);
        model.addAttribute("customer", customer);
        model.addAttribute("name", principal.getName());

        return "profile";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editGet(Model model) {
        Customer customer = customerService
                .getByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("customer", customer);

        return "edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editPost(@ModelAttribute("customer") Customer customer) {
        customerService.edit(customer);

        return "redirect:/profile/" + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchPost(@RequestParam("search") String search, Model model) {
        List<Customer> customers = customerService.search(search);
        model.addAttribute("customers", customers);

        return "search";
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public String getFriends(Model model) {
        List<Customer> customers = customerService.getFriends(
                SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("customers", customers);

        return "friends";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam("login") String login) {
        customerService.delete(login);

        return "redirect:/profile/friends";
    }

    @RequestMapping(value = "/addFriend", method = RequestMethod.GET)
    public String addFriend(@RequestParam("login") String login) {
        customerService.addFriend(login);

        return "redirect:/profile/" + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/acceptFriend", method = RequestMethod.GET)
    public String acceptFriend(@RequestParam("id") String uuid) {
        customerService.acceptFriend(uuid);

        return "redirect:/profile/notifications";
    }

    @RequestMapping(value = "/rejectFriend", method = RequestMethod.GET)
    public String rejectFriend(@RequestParam("id") String uuid) {
        customerService.rejectFriend(uuid);

        return "redirect:/profile/notifications";
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public String getNotifications(Model model) {
        Map<Notification, String> notifications = customerService.getNotifications(
                SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("notifications", notifications);

        return "notifications";
    }
}