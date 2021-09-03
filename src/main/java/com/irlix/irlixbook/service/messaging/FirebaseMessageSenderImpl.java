package com.irlix.irlixbook.service.messaging;

import com.google.firebase.messaging.*;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
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
                .setTitle(LocalDateTime.now() + "-" + LocalDateTime.now() + " " + title)
                .setBody(text)
                .build();

        AndroidNotification androidNotification = AndroidNotification
                .builder()
                .setClickAction("com.irlixsocialnetwork")
                .build();

        AndroidConfig androidConfig = AndroidConfig
                .builder()
                .setNotification(androidNotification)
                .build();

        Message message = Message
                .builder()
                .setToken(receiver)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();

        try {
            String send = firebaseMessaging.send(message);
            log.info("SEND" + send);
        } catch (FirebaseMessagingException e) {
            log.error("Send firebase push message error: {}", e.getMessage());
        }
    }

    @Override
    public void send(String title, String receiver, String text, Long id, ContentType type) {
        Notification notification = Notification
                .builder()
                .setTitle(LocalDateTime.now() + "-" + LocalDateTime.now() + " " + title)
                .setBody(text)
                .build();

        AndroidNotification androidNotification = AndroidNotification
                .builder()
                .setClickAction("com.irlixsocialnetwork")
                .build();

        AndroidConfig androidConfig = AndroidConfig
                .builder()
                .setNotification(androidNotification)
                .build();

        Message message = Message
                .builder()
                .setToken(receiver)
                .setNotification(notification)
                .putData("contentId", String.valueOf(id))
                .putData("type", String.valueOf(type))
                .setAndroidConfig(androidConfig)
                .build();


        try {
            String send = firebaseMessaging.send(message);
            log.info("SEND" + send);
        } catch (FirebaseMessagingException e) {
            log.error("Send firebase push message error: {}", e.getMessage());
        }
    }
}
