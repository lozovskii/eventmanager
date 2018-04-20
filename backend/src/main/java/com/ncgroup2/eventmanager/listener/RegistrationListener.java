package com.ncgroup2.eventmanager.listener;

import org.springframework.stereotype.Component;

@Component
public class RegistrationListener{// implements ApplicationListener<OnRegistrationCompleteEvent> {
//
//    @Autowired
//    private CustomerService service;
//
//    @Autowired
//    private MessageSource messages;
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Override
//    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
//        this.confirmRegistration(event);
//    }
//
//    private void confirmRegistration(OnRegistrationCompleteEvent event) {
//        Customer customer = event.getCustomer();
//        String token = UUID.randomUUID().toString();
//        service.createVerificationToken(customer, token);
//
//        String recipientAddress = customer.getEmail();
//        String subject = "Registration Confirmation";
//        String confirmationUrl
//                = event.getAppUrl() + "/registrationConfirm?token=" + token;
//        String message = "Confirmation link: \n";
//
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(recipientAddress);
//        email.setSubject(subject);
//        //email.setText(message  + "http://localhost:8090" + confirmationUrl);
//        // changed link for heroku address
//        email.setText(message  + "https://rocky-dusk-73382.herokuapp.com" + confirmationUrl);
//        mailSender.send(email);
//    }
}