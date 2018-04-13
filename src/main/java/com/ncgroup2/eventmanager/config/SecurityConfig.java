package com.ncgroup2.eventmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USER = "SELECT USERNAME, PASSWORD, ENABLED FROM \"USER\" WHERE USERNAME=?";
    private static final String AUTHORITIES = "SELECT USERNAME, ROLE FROM \"ROLE\" WHERE USERNAME=?";

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(USER)
                .authoritiesByUsernameQuery(AUTHORITIES);
    }

    @Override
    protected void configure(HttpSecurity http)  throws Exception  {
        http.authorizeRequests().antMatchers("/", "home").permitAll()
                .antMatchers("/user").hasRole("USER")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll();
    }
}
