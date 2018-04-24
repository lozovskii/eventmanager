package com.ncgroup2.eventmanager.listener;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.event.OnRegistrationCompleteEvent;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private CustomerService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        Customer customer = event.getCustomer();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(customer, token);

        String recipientAddress = customer.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = "Confirmation link: \n";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        //email.setText(message  + "http://localhost:8090" + confirmationUrl);
        // changed link for heroku address
        email.setText(message  + "https://rocky-dusk-73382.herokuapp.com" + confirmationUrl);
        mailSender.send(email);
    }
}