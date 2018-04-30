package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.WishList;
import com.ncgroup2.eventmanager.service.entityservice.impl.WishListServiceImpl;
import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @Autowired
    WishListServiceImpl wishListService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String addCustomer(Model model) {

        model.addAttribute("customer", new Customer());

        // TEST COMMANDS

//        WishList wishList = wishListService.getWishListByEventId();
////        WishList wishList = wishListService.getWishListByBookerId();
//
//        System.out.println("WishList ID: "+ wishList.getId());
//
//        for (ItemWishListDto item : wishList.getItems()) {
//            System.out.println("\n\tEventWishlistID: "+item.getEvent_wishlist_id());
//            System.out.println("\tItemWishlistID: "+item.getItem_wishlist_id());
//            System.out.println("\tBookerID: "+item.getBooker_customer_id());
//            System.out.println("\tItemID: "+item.getItem().getId());
//            System.out.println("\tItemName: "+item.getItem().getName());
//            System.out.println("\tItemDescription: "+item.getItem().getDescription());
//            System.out.println("\tItemLink: "+item.getItem().getLink());
//        }

        return "index";
    }

}
