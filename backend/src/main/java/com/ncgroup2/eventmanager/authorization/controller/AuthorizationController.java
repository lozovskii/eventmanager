package com.ncgroup2.eventmanager.authorization.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ncgroup2.eventmanager.authorization.model.AuthResponse;
import com.ncgroup2.eventmanager.authorization.model.UserAuthParam;
import com.ncgroup2.eventmanager.authorization.service.TokenGenerator;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    private static final String GOOGLE_CLIENT_ID = "882385907365-t3b1b4nieo5c2rna6ejf862eadkho2s2.apps.googleusercontent.com";
    private final TokenGenerator tokenGenerator;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private CustomerService customerService;

    @Autowired
    public AuthorizationController(TokenGenerator tokenGenerator, AuthenticationManager authenticationManager, CustomerService customerService, PasswordEncoder passwordEncoder) {
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth")
    public AuthResponse login(@RequestBody UserAuthParam userAuthParam) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userAuthParam.getLogin(), userAuthParam.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Customer customer = customerService.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        String token = tokenGenerator.generateToken(customer);
        return new AuthResponse(token);
    }

    @PostMapping("/auth/google")
    public ResponseEntity googleLogin(@RequestBody Customer googleCustomer) throws Exception {

        String googleId = getGoogleId(googleCustomer.getToken());
        if (googleId == null) {
            return new ResponseEntity("Invalid user", HttpStatus.BAD_REQUEST);
        }

        Customer authCustomer = customerService.getByGoogleId(googleId);
        if (authCustomer != null) {
            String JWTToken = tokenGenerator.generateToken(authCustomer);
            return new ResponseEntity(new AuthResponse(JWTToken), HttpStatus.OK);
        }

        return registerGoogleCustomer(googleCustomer, googleId);


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

            return payload.getSubject();
        } else {
            return null;
        }
    }

    private ResponseEntity registerGoogleCustomer(Customer googleCustomer, String googleId) {
        if (!customerService.isEmailUnique(googleCustomer.getEmail())) {
            customerService.addGoogleId(googleCustomer.getEmail(), googleId);
            String JWTToken = tokenGenerator.generateToken(customerService.getByGoogleId(googleId));
            return new ResponseEntity(new AuthResponse(JWTToken), HttpStatus.OK);
        }

        Random random = new Random();
        while (customerService.isCustomerPresent(googleCustomer.getLogin())) {
            googleCustomer.setLogin(googleCustomer.getLogin() + random.nextInt(9));
        }

        googleCustomer.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        googleCustomer.setToken("");
        googleCustomer.setVerified(true);
        customerService.register(googleCustomer);
        customerService.addGoogleId(googleCustomer.getEmail(), googleId);
        String JWTToken = tokenGenerator.generateToken(customerService.getByLogin(googleCustomer.getLogin()));
        return new ResponseEntity(new AuthResponse(JWTToken), HttpStatus.OK);
    }

}
