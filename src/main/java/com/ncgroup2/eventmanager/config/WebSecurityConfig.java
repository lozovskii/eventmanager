package com.ncgroup2.eventmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//        .antMatchers("/css/**").permitAll()
//        .antMatchers("/", "home", "/register","/registration/*","/registrationConfirm").permitAll()
//                .antMatchers("/hello").hasRole("USER")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/login").permitAll()
//                .and()
//                .logout().permitAll()
//                .and()
//                .csrf().disable();
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/").permitAll();

        http.authorizeRequests().antMatchers("/login","logout").anonymous();

        http.authorizeRequests().antMatchers("/register","/registration**", "registrationConfirm").anonymous();

        http.authorizeRequests().antMatchers("/hello").authenticated();

        http.authorizeRequests().and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/hello")
                .and().logout().logoutSuccessUrl("/login");
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}