package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping(value = "/created")
    public ResponseEntity<Collection<Item>> getCreatedItems(@RequestParam String customerLogin) {

        Collection<Item> createdItems = itemService.getCreatedItems(customerLogin);

        if (createdItems == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(createdItems, HttpStatus.OK);
    }
}
