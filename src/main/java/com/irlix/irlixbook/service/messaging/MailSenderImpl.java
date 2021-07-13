package com.irlix.irlixbook.service.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("mailSenderImpl")
@Slf4j
@RequiredArgsConstructor
public class MailSenderImpl implements MessageSender {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void send(String title, String receiver, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject(title);
        message.setText(text);
        emailSender.send(message);
    }
}
