package com.irlix.irlixbook.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void send(String receiver, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject("Your password");
        message.setText("Your password: " + text);
        emailSender.send(message);
    }
}
