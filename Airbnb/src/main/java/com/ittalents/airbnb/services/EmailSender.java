package com.ittalents.airbnb.services;

import com.ittalents.airbnb.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender{
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String email, String body, String subject){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("phangelov@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
