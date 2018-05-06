package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.WishList;
import com.ncgroup2.eventmanager.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private WishListService wishListService;

    @Autowired
    public WishListController(WishListService wishListService){
        this.wishListService = wishListService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<WishList> getWishList1(@PathVariable("id") String id) {

        if (id.isEmpty()) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } else {

            WishList wishList = wishListService.getByEventId(id);

            if (wishList == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(wishList, HttpStatus.OK);
        }
    }

//    @GetMapping(value = "/show-wishlist/{id}")
//    public WishList getWishList(@RequestParam String eventId) {
//        return wishListService.getByEventId(eventId);
//    }

}
