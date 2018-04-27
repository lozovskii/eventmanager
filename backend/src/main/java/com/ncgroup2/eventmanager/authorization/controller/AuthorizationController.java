package com.ncgroup2.eventmanager.authorization.controller;

import com.ncgroup2.eventmanager.authorization.model.AuthResponse;
import com.ncgroup2.eventmanager.authorization.model.UserAuthParam;
import com.ncgroup2.eventmanager.authorization.service.TokenGenerator;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    private final TokenGenerator tokenGenerator;
    private AuthenticationManager authenticationManager;
    private CustomerService customerService;

    @Autowired
    public AuthorizationController(TokenGenerator tokenGenerator, AuthenticationManager authenticationManager, CustomerService customerService) {
        this.tokenGenerator = tokenGenerator;
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
    }

    @PostMapping("/auth")
    public AuthResponse login(@RequestBody UserAuthParam userAuthParam) throws AuthenticationException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userAuthParam.getLogin(), userAuthParam.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Customer customer = customerService.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        String token = tokenGenerator.generateToken(customer);
        return new AuthResponse(token);
    }
}
