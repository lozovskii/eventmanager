package com.ncgroup2.eventmanager.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

@Configuration
public class AuthenticationProvider {

    private final DataSource dataSource;

    @Autowired
    public AuthenticationProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {

        JdbcDaoImpl impl = new JdbcDaoImpl();

        impl.setDataSource(dataSource);
        impl.setUsersByUsernameQuery("SELECT login, password, isverified FROM \"Customer\" WHERE login = ?");
        impl.setAuthoritiesByUsernameQuery("SELECT login, 'USER' FROM \"Customer\" WHERE login = ?");

        return impl;
    }
}