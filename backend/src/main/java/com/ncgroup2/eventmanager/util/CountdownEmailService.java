package com.ncgroup2.eventmanager.util;

import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CountdownEmailService {
    public final static long ONE_DAY_IN_MS = 86_400_000L;

    private EventService eventService;
    private JavaMailSender mailSender;

    @Autowired
    public CountdownEmailService(EventService eventService, JavaMailSender mailSender) {
        this.eventService = eventService;
        this.mailSender = mailSender;
    }

    @Scheduled(fixedDelay = ONE_DAY_IN_MS)
    public void sendCountdownEmails() {
        eventService.getCountdownMessages().forEach(event
                -> sendMessages(event.getEmail(), event.getMessage()));
    }

    private void sendMessages(String email, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Don't forget!");
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
        System.out.println(message + " sent to " + email);


    }
}
