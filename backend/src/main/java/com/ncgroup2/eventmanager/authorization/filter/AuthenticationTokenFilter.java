package com.ncgroup2.eventmanager.authorization.filter;

import com.ncgroup2.eventmanager.authorization.model.AuthenticationBox;
import com.ncgroup2.eventmanager.authorization.service.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends GenericFilterBean {

    private static final Integer START_TOKEN_NUMBER = "Bearer ".length();
    private TokenGenerator tokenGenerator;

    @Autowired
    public AuthenticationTokenFilter(TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String jwt = header.substring(START_TOKEN_NUMBER);
            if (jwt.isEmpty()) {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
            UserDetails userDetails = this.tokenGenerator.getUserDetails(jwt);
            authentication = new AuthenticationBox(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
