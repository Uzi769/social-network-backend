package com.irlix.irlixbook.service.messaging;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("firebaseSender")
@Slf4j
@RequiredArgsConstructor
public class FirebaseMessageSenderImpl implements MessageSender {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void send(String title, String receiver, String text) {
        Notification notification = Notification
                .builder()
                .setTitle(LocalDateTime.now() + "-" + LocalDateTime.now())
                .setBody(text)
                .build();

        Message message = Message
                .builder()
                .setToken(receiver)
                .setNotification(notification)
                .build();

        try {
            String send = firebaseMessaging.send(message);
            log.info("SEND" + send);
        } catch (FirebaseMessagingException e) {
            log.error("Send firebase push message error: {}", e.getMessage());
        }
    }
}
