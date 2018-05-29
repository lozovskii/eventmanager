package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Page;
import com.ncgroup2.eventmanager.objects.ExtendedTag;
import com.ncgroup2.eventmanager.entity.Item;
import com.ncgroup2.eventmanager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @PostMapping(value = "/batch-create")
    public void createItems(@RequestBody Collection<Item> item) {
        itemService.createItems(item);
    }

    @PostMapping(value = "/create")
    public void createItem(@RequestBody Item item) {
        itemService.createItem(item);
    }

    @PutMapping(value = "/update")
    public void updatetem(@RequestBody Item item) {
        itemService.update(item);
    }

    @PostMapping(value = "/batch-delete")
    public void deleteItems(@RequestBody List<Item> trash) {
        itemService.deleteItems(trash);
    }

    @PostMapping(value = "/batch-delete-tags")
    public void deleteTags(@RequestBody List<ExtendedTag> trash) {
        itemService.removeTags(trash);
    }

    @GetMapping(value = "/collection")
    public ResponseEntity<Collection<Item>> getCreatedItems(@RequestParam String customerLogin) {
        Collection<Item> createdItems = itemService.getCreatedItems(customerLogin);

        if (createdItems == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(createdItems, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Collection<Item>> getAllItems() {
        Collection<Item> items = itemService.getAllItems();

        if (items == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<Collection<Item>> getPopularItems() {
        Collection<Item> items = itemService.getPopularItems();

        if (items == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping(value = "/pageAll")
    public Page<Item> getAllItems(@RequestParam int pageNo, @RequestParam int pageSize) {
        return itemService.getAllItems(pageNo, pageSize);
    }

    @PutMapping(value = "/update-rating")
    public ResponseEntity updateRating(@RequestBody String[] params) {
        itemService.updateRating(params[0],params[1]);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
