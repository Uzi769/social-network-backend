package com.irlix.irlixbook.service.messaging;

import com.irlix.irlixbook.dao.entity.enams.ContentType;

public interface MessageSender {

    void send(String title, String receiver, String text);

    void send(String title, String receiver, String text, Long id, ContentType type);

}
