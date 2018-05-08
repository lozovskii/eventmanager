package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Relationship;
import com.ncgroup2.eventmanager.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
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

//    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity update(@RequestBody Customer customer) {
//
//        //If you use POST, it's work
//
//        return new ResponseEntity(HttpStatus.OK);
//
//    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseStatus( HttpStatus.OK )
    public void accept(@RequestBody Customer customer) {
        System.out.println("Controler" + customer.toString());
        customerService.edit(customer);

    }


//    @RequestMapping(value = "edit/upload", method = RequestMethod.PUT)
//    @ResponseStatus( HttpStatus.OK )
//    public ResponseEntity avatarUpload(MultipartFile file) {
//        if (file.isEmpty()) {
//                return new ResponseEntity(HttpStatus.BAD_REQUEST);
//            }
//
//            customer.setAvatar(encodeToString(file.getBytes()));
//            customerService.uploadAvatar(customer);
//
//        return ResponseEntity(HttpStatus.OK);
//    }

    @PostMapping(value = "edit/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String avatarUploadPost(@RequestParam("file") MultipartFile file, @ModelAttribute("customer") Customer customer,
                                   RedirectAttributes attributes) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file");
            return "redirect:/profile/edit/upload/status";
        }

        try {
            customer.setAvatar(Base64Utils.encodeToString(file.getBytes()));
            customerService.uploadAvatar(customer);
            attributes.addFlashAttribute("message", "Successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profile/edit/upload/status";
    }

    @GetMapping(value = "edit/upload/status", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String uploadStatus(Principal principal, Model model) {
        model.addAttribute("name", principal.getName());
        return "/profile/status";
    }


    @GetMapping(value = "edit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Customer> editGet(@RequestParam String login) {
        if (!login.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Customer customer = customerService.getByLogin(login);

            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
    }

    @PostMapping(value = "edit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Customer> editPost(@RequestBody Customer customer) {
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            customerService.edit(customer);

            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
    }

    @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Customer>> search(@RequestParam String search) {
        List<Customer> customers = customerService.search(search);

        return new ResponseEntity<>(customers, HttpStatus.OK);
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

    @GetMapping(value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpStatus delete(@RequestParam String login) {
        try {
            customerService.delete(login);

            return HttpStatus.OK;
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
        if (!login.equals("aliashchuk")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            List<Relationship> notifications = customerService.getNotifications(login);

            return new ResponseEntity<>(notifications, HttpStatus.OK);
        }
    }
}