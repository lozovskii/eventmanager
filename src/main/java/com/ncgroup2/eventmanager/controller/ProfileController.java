package com.ncgroup2.eventmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfile(Model model) {

        return "profile/profile";

    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String showProfile2(Model model) {

        return "profile/profile";

    }
}
