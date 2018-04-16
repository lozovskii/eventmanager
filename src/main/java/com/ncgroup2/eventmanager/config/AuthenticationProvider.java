package com.ncgroup2.eventmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

@Configuration
public class AuthenticationProvider {

    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");

        return dataSource;
    }

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        JdbcDaoImpl impl = new JdbcDaoImpl();

        impl.setDataSource(dataSource());
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