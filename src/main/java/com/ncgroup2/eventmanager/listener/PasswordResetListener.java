package com.ncgroup2.eventmanager.listener;

import com.ncgroup2.eventmanager.entity.Customer;
import com.ncgroup2.eventmanager.event.OnPasswordResetEvent;
import com.ncgroup2.eventmanager.service.entityservice.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

public class PasswordResetListener implements ApplicationListener<OnPasswordResetEvent> {

    @Autowired
    private CustomerService service;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnPasswordResetEvent event) {
        this.resetPassword(event);
    }

    private void resetPassword(OnPasswordResetEvent event) {

        Customer customer = event.getCustomer();
        String token = UUID.randomUUID().toString();

        service.createVerificationToken(customer, token);

        String recipientAddress = customer.getEmail();

        String subject = "Password reset";

        String confirmationUrl
                = event.getAppUrl() + "/resetPassword?token=" + token;

        String message = "Confirmation link: \n";

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(recipientAddress);
        email.setSubject(subject);

        email.setText(message  + "https://rocky-dusk-73382.herokuapp.com" + confirmationUrl);

        mailSender.send(email);
    }

}
