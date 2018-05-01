package com.ncgroup2.eventmanager.util;

import com.ncgroup2.eventmanager.dto.EventCountdownDTO;
import com.ncgroup2.eventmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
        List<EventCountdownDTO> countdowns = eventService.getCountdownMessages();

        for (EventCountdownDTO countdown : countdowns) {
            sendMessages(countdown.getEmail(), countdown.getMessages());
        }

    }

    private void sendMessages(String email, String messages) {
        System.out.println(messages);
        String[] messageArray = messages.split("\\|");

        for(String message : messageArray) {
            System.out.println(message + ' '+ email);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Don't forget!");
            simpleMailMessage.setText(message);
            mailSender.send(simpleMailMessage);
        }

    }
}
