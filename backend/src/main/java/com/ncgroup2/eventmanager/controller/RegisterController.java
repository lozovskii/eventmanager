package com.ncgroup2.eventmanager.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ncgroup2.eventmanager.authorization.model.AuthResponse;
import com.ncgroup2.eventmanager.authorization.service.TokenGenerator;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.CustomerService;
import com.ncgroup2.eventmanager.service.sender.MyMailSender;
import com.ncgroup2.eventmanager.service.sender.SubjectEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private static final String GOOGLE_CLIENT_ID = "882385907365-t3b1b4nieo5c2rna6ejf862eadkho2s2.apps.googleusercontent.com";
    private final TokenGenerator tokenGenerator;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final MyMailSender mailMyMailSender;

    @Autowired
    public RegisterController(TokenGenerator tokenGenerator, CustomerService customerService, PasswordEncoder passwordEncoder, MyMailSender mailMyMailSender) {
        this.tokenGenerator = tokenGenerator;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.mailMyMailSender = mailMyMailSender;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addUser(@RequestBody Customer customer) {
        if (customerService.isCustomerPresent(customer.getLogin())) {
            return new ResponseEntity("This login is already taken", HttpStatus.BAD_REQUEST);
        }

        if (!customerService.isEmailUnique(customer.getEmail())) {
            return new ResponseEntity("This email is already taken", HttpStatus.BAD_REQUEST);
        }

        String token = UUID.randomUUID().toString();
        customer.setToken(token);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        customerService.register(customer);

        mailMyMailSender.sendEmail(customer.getEmail(), SubjectEnum.REGISTRATION, token);

        return new ResponseEntity(HttpStatus.OK);
    }

    //    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    @ResponseStatus
    @GetMapping(value = "/registrationConfirm")
    public ResponseEntity confirmRegistration
    (@RequestParam String token) {
        Customer customer = customerService.getCustomerByToken(token);
        if (customer == null) {
            return new ResponseEntity("Your token is invalid", HttpStatus.BAD_REQUEST);
        }
        Instant expireDate = customer.getRegistrationDate().plus(24, ChronoUnit.HOURS);

        if (Instant.now().isAfter(expireDate)) {
            customerService.deleteCustomer(customer);
            return new ResponseEntity("Your token is expired. Please, register again", HttpStatus.BAD_REQUEST);
        }

        customerService.confirmCustomer(customer);


        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/googleRegister")
    public ResponseEntity googleRegister(@RequestBody Customer customer) throws Exception {
        if (!customerService.isEmailUnique(customer.getEmail())) {
            String googleId = getGoogleId(customer.getToken());
            customerService.addGoogleId(customer.getEmail(), googleId);
            String JWTToken = tokenGenerator.generateToken(customerService.getByGoogleId(googleId));
            return new ResponseEntity(new AuthResponse(JWTToken), HttpStatus.OK);
        }

        Random random = new Random();
        while (customerService.isCustomerPresent(customer.getLogin())) {
            customer.setLogin(customer.getLogin()+random.nextInt(9));
        }


        String googleId = getGoogleId(customer.getToken());
        if (googleId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


        customer.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        customer.setToken("");
        customer.setVerified(true);
        customerService.register(customer);
        customerService.addGoogleId(customer.getEmail(), googleId);
        String JWTToken = tokenGenerator.generateToken(customerService.getByLogin(customer.getLogin()));
        return new ResponseEntity(new AuthResponse(JWTToken), HttpStatus.OK);
    }


    private String getGoogleId(String token) throws Exception {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, JacksonFactory.getDefaultInstance())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

// (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            return payload.getSubject();
        } else {
            return null;
        }
    }


}
