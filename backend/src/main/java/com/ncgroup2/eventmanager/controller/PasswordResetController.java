package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.dto.PasswordResetDTO;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.CustomerService;
import com.ncgroup2.eventmanager.service.sender.MyMailSender;
import com.ncgroup2.eventmanager.service.sender.SubjectEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reset")
public class PasswordResetController {

    private final CustomerService customerService;

    private final MyMailSender mailMyMailSender;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetController(CustomerService customerService, MyMailSender mailMyMailSender, PasswordEncoder passwordEncoder) {
        this.customerService = customerService;
        this.mailMyMailSender = mailMyMailSender;
        this.passwordEncoder = passwordEncoder;
    }


    //    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @GetMapping(value = "/sendLink")
    public ResponseEntity resetPassword(@RequestParam String email) {

        System.out.println(email);

        if (customerService.isEmailUnique(email)) {
            return new ResponseEntity("This email is not valid", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerService.getCustomerByEmail(email);

        String token = UUID.randomUUID().toString();

        customerService.createVerificationToken(customer, token);

        mailMyMailSender.sendEmail(customer.getEmail(), SubjectEnum.RESET_PASSWORD, token);

        return new ResponseEntity(HttpStatus.OK);
    }

    //    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    @GetMapping(value = "/resetPassword")
    public ResponseEntity displayResetPasswordPage(@RequestParam("token") String token) {

        Customer customer = customerService.getCustomerByToken(token);

        if (customer == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/setNewPassword", method = RequestMethod.POST)
    public ResponseEntity setPassword(
            @RequestBody PasswordResetDTO passwordDTO) {

        Customer customer = customerService.getCustomerByToken(passwordDTO.getToken());

//        System.out.println(token);
//
//        System.out.println(customer.getEmail());

        if (customer == null) {

            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        }

        customer.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
        customerService.updatePassword(customer);

        return new ResponseEntity(HttpStatus.OK);

    }


}



