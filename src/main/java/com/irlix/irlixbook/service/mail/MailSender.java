package com.irlix.irlixbook.service.mail;

public interface MailSender {

    void send(String receiver, String text);
}
