package com.ncgroup2.eventmanager.config;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class WebController {

    @RequestMapping(value = "/")
    public String home(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getName());
        } else {
            model.addAttribute("name", null);
        }
        return "home";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }
}
