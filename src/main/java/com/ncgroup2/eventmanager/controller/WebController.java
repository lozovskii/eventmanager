package com.ncgroup2.eventmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value = "/")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/user")
    public String user() {
        return "user";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }
}
