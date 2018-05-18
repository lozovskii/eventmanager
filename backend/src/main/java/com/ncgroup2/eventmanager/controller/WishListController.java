package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.dto.ItemWishListDto;
import com.ncgroup2.eventmanager.entity.WishList;
import com.ncgroup2.eventmanager.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private WishListService wishListService;

    @Autowired
    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<WishList> getWishList(@PathVariable("id") String id) {
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

    @GetMapping(value = "/booked")
    public ResponseEntity<WishList> getBookedItems(@RequestParam String customerLogin) {
        WishList bookedItems = wishListService.getBookedItems(customerLogin);

        if (bookedItems == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(bookedItems, HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public void update(@RequestBody WishList wishList) {
        wishListService.update(wishList);
    }

    @PostMapping(value = "/add")
    public void addItems(@RequestBody WishList wishList) {
        wishListService.create(wishList);
    }

    @PostMapping(value = "/delete")
    public void removeItems(@RequestBody List<ItemWishListDto> trash) {
        wishListService.deleteItems(trash);
    }

}
