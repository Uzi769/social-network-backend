package com.irlix.irlixbook.service.messaging;

public interface MessageSender {

    void send(String title, String receiver, String text);

    void send(String title, String receiver, String text, Long id);
}
