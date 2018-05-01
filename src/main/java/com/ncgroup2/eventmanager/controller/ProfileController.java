package com.ncgroup2.eventmanager.controller;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.entity.Relationship;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static com.ncgroup2.eventmanager.service.upload.img.ImageFileValidator.isValid;
import static org.springframework.util.Base64Utils.encodeToString;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private CustomerService customerService;

    @Autowired
    public ProfileController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/{login}")
    public String getCustomerByLogin(@PathVariable("login") String login, Principal principal, Model model) {
        Customer customer = customerService.getByLogin(login);
        model.addAttribute("customer", customer);
        model.addAttribute("name", principal.getName());

        return "profile/profile" ;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editGet(Model model) {
        Customer customer = customerService
                .getByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("customer", customer);

        return "profile/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editPost(@ModelAttribute("customer") Customer customer) {
        customerService.edit(customer);

        return "redirect:/profile/" + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchPost(@RequestParam("search") String search, Model model) {
        List<Customer> customers = customerService.search(search);
        model.addAttribute("customers", customers);

        return "profile/search";
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public String getFriends(Model model) {
        List<Customer> customers = customerService.getFriends(
                SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("customers", customers);

        return "profile/friends";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam("login") String login) {
        customerService.delete(login);

        return "redirect:/profile/friends";
    }

    @RequestMapping(value = "/addFriend", method = RequestMethod.GET)
    public String addFriend(@RequestParam("login") String login) {
        customerService.addFriend(login);

        return "redirect:/profile/" + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/acceptFriend", method = RequestMethod.GET)
    public String acceptFriend(@RequestParam("token") String token) {
        customerService.acceptFriend(token);

        return "redirect:/profile/notifications";
    }

    @RequestMapping(value = "/rejectFriend", method = RequestMethod.GET)
    public String rejectFriend(@RequestParam("token") String token) {
        customerService.rejectFriend(token);

        return "redirect:/profile/notifications";
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public String getNotifications(Model model) {
        List<Relationship> relationships = customerService.getNotifications(
                SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("relationships", relationships);

        return "profile/notifications";
    }

    @RequestMapping(value = "/edit/upload", method = RequestMethod.GET)
    public String avatarUploadGet(Model model) {
        Customer customer = customerService
                .getByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("customer", customer);

        return "profile/upload";
    }

    @RequestMapping(value = "/edit/upload", method = RequestMethod.POST)
    public String avatarUploadPost(@RequestParam("file") MultipartFile file, @ModelAttribute("customer") Customer customer,
                                   RedirectAttributes attributes) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file");
            return "redirect:/profile/edit/upload/status";
        }

        if(isValid(file)) {
            try {
                customer.setAvatar(encodeToString(file.getBytes()));
                customerService.uploadAvatar(customer);
                attributes.addFlashAttribute("message", "Successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "redirect:/profile/edit/upload/status";
        }else { attributes.addFlashAttribute("message", "Invalid file extension");
        return "redirect:/profile/edit/upload/status";}
    }

    @RequestMapping(value = "/edit/upload/status", method = RequestMethod.GET)
    public String uploadStatus(Principal principal, Model model) {
        model.addAttribute("name", principal.getName());
        return "profile/status";
    }
}