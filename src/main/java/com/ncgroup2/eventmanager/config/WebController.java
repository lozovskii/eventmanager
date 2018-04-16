package com.ncgroup2.eventmanager.config;

        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value = "/")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }
}
