package com.ncgroup2.eventmanager.service.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@PropertySource("classpath:properties/server.properties")
@Component
public class MyMailSender {

    private final JavaMailSender mailSender;

    private String recipientAddress;

    private String subject;

    private String confirmationUrl;

    private String text;

    @Value("${host}")
    private String host;

    @Autowired
    public MyMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String recipientAddress, SubjectEnum subjectEnum, String token) {

        String message = "Confirmation link: \n";

        this.recipientAddress = recipientAddress;

        switch (subjectEnum){

            case REGISTRATION: {
                this.subject = "Registration";
                this.confirmationUrl = "/registration-confirm";

            } break;

            case FRIEND_REQUEST: {
                this.subject = "Friend request";
                this.confirmationUrl = "/acceptRequest";

            } break;

            case RESET_PASSWORD: {
                this.subject = "Reset password";
                this.confirmationUrl = "/reset-password";

            } break;

            case EVENT_NOTIFICATION: {
                this.subject = "Request to event";
                this.confirmationUrl = "/acceptRequest";

            } break;
        }

        this.subject += " confirmation";

        this.confirmationUrl += "?token=" + token;

        this.text = message + host + confirmationUrl;

        send();

    }

    public void sendBasicEmailWithLink(String sendTo, String subject, String message, String link) {
        recipientAddress = sendTo;
        this.subject = subject;
        text = message + host + link;
        send();
    }

    private void send() {

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(recipientAddress);

        email.setSubject(subject);

        email.setText(text);

        mailSender.send(email);
    }

    public void sendMessage(String email) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setSubject("Your events");
            helper.setText("All events in which you participate");
            helper.setTo(email);

            helper.addAttachment(
                    SecurityContextHolder.getContext().getAuthentication().getName() + ".pdf",
                    new FileSystemResource(new File(
                            SecurityContextHolder.getContext().getAuthentication().getName() + ".pdf")));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }
}

