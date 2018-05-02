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

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    WishListServiceImpl wishListService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String addCustomer(Model model) {

        model.addAttribute("customer", new Customer());

//        TEST COMMANDS

//        WishList newWishList = new WishList();
//
//        newWishList.setId("7691ba6c-4d1c-11e8-8b6a-d79631e9e952");
//
//        List<ItemWishListDto> list = new ArrayList<>();
//
//        ItemWishListDto listDto = new ItemWishListDto();
//
//        listDto.setItem_wishlist_id("e725aa58-4d1d-11e8-953d-670ee868ff4e");
//
//        list.add(listDto);
//
//        newWishList.setItems(list);
//
//        wishListService.createNewWishlist(newWishList);

//        WishList wishList1 = wishListService.getByEventId("7691ba6c-4d1c-11e8-8b6a-d79631e9e952");
//        WishList wishList = wishListService.getByEventId("1f0a7864-4d1d-11e8-8b72-df859189b814");
        WishList wishList = wishListService.getBookedItems("1ba970a2-4c97-11e8-893d-bbb831619495");

//        WishList wishList = wishListService.getById("7ca297d0-4d1e-11e8-ab13-7b6e01298124");

//        wishList1.getItems().get(0).setBooker_customer_id(null);
//        wishList1.getItems().get(0).setPriority("2");

//      TODO: ERROR, field booker_customer_id REFERENCES field which not allow nulls
//        wishListService.updateByField(
//                wishList1.getItems().get(0).getItem_wishlist_id(),
//                "booker_customer_id",
//                wishList1.getItems().get(0).getBooker_customer_id()
//                );

//        wishListService.updateByField(
//                wishList1.getItems().get(0).getItem_wishlist_id(),
//                "priority",
//                wishList1.getItems().get(0).getPriority()
//        );
//
//        WishList wishList = wishListService.getByEventId("7691ba6c-4d1c-11e8-8b6a-d79631e9e952");
//
//        System.out.println("\nWishList ID: "+ wishList.getId());
//
//        for (ItemWishListDto item : wishList.getItems()) {
//            System.out.println("\n\tEventWishlistID: "+item.getEvent_wishlist_id());
//            System.out.println("\tEventID: "+item.getEvent_id());
//            System.out.println("\tItemWishlistID: "+item.getItem_wishlist_id());
//            System.out.println("\tItemPriority: "+item.getPriority());
//            System.out.println("\tBookerID: "+item.getBooker_customer_id());

//            System.out.println("\tItemID: "+item.getItem().getId());
//            System.out.println("\tItemName: "+item.getItem().getName());
//           System.out.println("\tItemDescription: "+item.getItem().getDescription());
//           System.out.println("\tItemLink: "+item.getItem().getLink());
//        }

        return "index";
    }

}
