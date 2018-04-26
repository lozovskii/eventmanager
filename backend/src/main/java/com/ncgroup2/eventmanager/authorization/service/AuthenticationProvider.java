package com.ncgroup2.eventmanager.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

@Configuration
public class AuthenticationProvider {

    @Autowired
    DataSource dataSource;

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {

        JdbcDaoImpl impl = new JdbcDaoImpl();

        impl.setDataSource(dataSource);
        impl.setUsersByUsernameQuery(
                "SELECT login, password, isverified FROM \"Customer\" WHERE login = ?"
        );
        impl.setAuthoritiesByUsernameQuery(
                "SELECT \"Customer\".login, \"Role\".name FROM \"Customer\" JOIN \"Customer_Role\" " +
                        "ON \"Customer\".id = \"Customer_Role\".customer_id JOIN \"Role\" " +
                        "ON \"Customer_Role\".role_id = \"Role\".id WHERE \"Customer\".login = ?"
        );

        return impl;
    }
}