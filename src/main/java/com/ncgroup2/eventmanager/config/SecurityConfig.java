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

    private static final String USER = "SELECT LOGIN, PASSWORD, TRUE FROM \"Customer\" WHERE LOGIN=?";
    private static final String AUTHORITIES = "select \"Customer\".login, \"Role\".name from \"Customer\" " + "join\"Customer_Role\" on \"Customer\".id = \"Customer_Role\".customer_id\n" +
            "join \"Role\" on \"Role\".id = \"Customer_Role\".role_id WHERE \"Customer\".login = ?";
    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(USER).authoritiesByUsernameQuery(AUTHORITIES);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "home", "/register","/registration/*","/registrationConfirm").permitAll()
                .antMatchers("/user").hasRole("USER")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll();
    }
}
