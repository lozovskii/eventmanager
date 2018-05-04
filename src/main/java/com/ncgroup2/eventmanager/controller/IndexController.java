package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.entityservice.impl.ItemServiceImpl;
import com.ncgroup2.eventmanager.service.entityservice.impl.WishListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @Autowired
    WishListServiceImpl wishListService;

    @Autowired
    ItemServiceImpl itemService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String addCustomer(Model model) {

        model.addAttribute("customer", new Customer());

        return "index";
    }

}
