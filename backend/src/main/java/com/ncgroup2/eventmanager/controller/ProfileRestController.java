package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Page;
import com.ncgroup2.eventmanager.entity.Relationship;
import com.ncgroup2.eventmanager.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/")
public class ProfileRestController {

    private final CustomerService customerService;

    @Autowired
    public ProfileRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "{login}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Customer> getCustomerByLogin(@PathVariable("login") String login) {
        if (login.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Customer customer = customerService.getByLogin(login);

            if (customer == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void edit(@RequestBody Customer customer) {
        customerService.edit(customer);
    }

    @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<Customer> search(@RequestParam int page, @RequestParam int size, @RequestParam String search) {
        return customerService.search(page, size, search);
    }

    @GetMapping(value = "friends", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Customer>> friends(@RequestParam String login) {
        if (!login.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Customer> friends = customerService.getFriends(login);

            return new ResponseEntity<>(friends, HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "delete/{login}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpStatus delete(@PathVariable String login) {
        try {
            customerService.delete(login);

            return HttpStatus.NO_CONTENT;
        } catch (Throwable e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @GetMapping(value = "add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpStatus addFriend(@RequestParam String login) {
        try {
            customerService.addFriend(login);

            return HttpStatus.OK;
        } catch (Throwable e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @GetMapping(value = "accept", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpStatus acceptFriendship(@RequestParam String token) {
        try {
            customerService.acceptFriend(token);

            return HttpStatus.OK;
        } catch (Throwable e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @GetMapping(value = "reject", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpStatus rejectFriendship(@RequestParam String token) {
        try {
            customerService.rejectFriend(token);

            return HttpStatus.OK;
        } catch (Throwable e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @GetMapping(value = "notifications", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Relationship>> getNotifications(@RequestParam String login) {
        if (!login.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Relationship> notifications = customerService.getNotifications(login);

            return new ResponseEntity<>(notifications, HttpStatus.OK);
        }
    }

    @GetMapping("isFriends")
    public ResponseEntity isFriends(@RequestParam String currentCustomerId, @RequestParam String customerId) {
        return customerService.isFriends(currentCustomerId,customerId) ?
                new ResponseEntity(HttpStatus.OK) :
                new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}