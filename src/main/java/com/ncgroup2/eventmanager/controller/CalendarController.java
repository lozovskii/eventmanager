package com.ncgroup2.eventmanager.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CalendarController {
    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    public String showCalendar(Model model) {

        return "calendar/calendar";

    }
}