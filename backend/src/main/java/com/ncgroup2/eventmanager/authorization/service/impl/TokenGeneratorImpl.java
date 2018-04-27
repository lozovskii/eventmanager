package com.ncgroup2.eventmanager.authorization.service.impl;

import com.ncgroup2.eventmanager.authorization.model.UserDetailsBox;
import com.ncgroup2.eventmanager.authorization.service.TokenGenerator;
import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import io.jsonwebtoken.Jwts;

@Component
@PropertySource("classpath:token.properties")
public class TokenGeneratorImpl implements TokenGenerator{

    @Value("${key}")
    private String key;
    private CustomerService customerService;

    @Autowired
    TokenGeneratorImpl(CustomerService customerService) {
        this.customerService=customerService;
    }

    @Override
    public String generateToken(Customer customer) {
        Instant tokenTimeAccess = Instant.now().plus(1,ChronoUnit.DAYS);
        Date tokenTime = Date.from(tokenTimeAccess);

        return Jwts.builder()
                .claim("id", customer.getId())
                .claim("login", customer.getLogin())
                .setExpiration(tokenTime)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    @Override
    public UserDetails getUserDetails(String jwt) {
        String login = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody()
                .get("login")
                .toString();
        Customer customer = customerService.findByLogin(login);
        return new UserDetailsBox(customer);
    }
}
