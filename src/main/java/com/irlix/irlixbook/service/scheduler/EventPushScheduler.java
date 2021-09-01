package com.irlix.irlixbook.service.scheduler;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.UserAppCode;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.repository.ContentRepository;
import com.irlix.irlixbook.repository.UserAppCodeRepository;
import com.irlix.irlixbook.service.messaging.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventPushScheduler {

    private final ContentRepository contentRepository;
    private final UserAppCodeRepository userAppCodeRepository;

    @Autowired
    @Qualifier("firebaseSender")
    private MessageSender messageSender;

    @Scheduled(cron = "0 */5 * * * *")
    public void pushMessageAboutEvent() {
        List<Content> events = contentRepository.findByType(ContentType.EVENT);
        for (Content event : events) {
            if ((event.getEventDate().isAfter(LocalDateTime.now().plusDays(1).minusSeconds(149)) &
                    event.getEventDate().isBefore(LocalDateTime.now().plusDays(1).plusSeconds(150))) |
                    (event.getEventDate().isAfter(LocalDateTime.now().plusHours(1).minusSeconds(149)) &
                    event.getEventDate().isBefore(LocalDateTime.now().plusHours(1).plusSeconds(150)))) {
                List<UserAppCode> listOfCodes = userAppCodeRepository.findAll();
                for (UserAppCode codes : listOfCodes) {
                    for (String code : codes.getCodes()) {
                        messageSender.send(event.getType() + " was created."
                                , code
                                , event.getName()
                                , event.getId()
                                , event.getType());
                    }
                }
            }
        }
    }
}
