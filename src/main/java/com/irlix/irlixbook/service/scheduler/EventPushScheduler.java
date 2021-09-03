package com.irlix.irlixbook.service.scheduler;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.ContentUser;
import com.irlix.irlixbook.dao.entity.UserAppCode;
import com.irlix.irlixbook.dao.entity.UserEntity;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventPushScheduler {

    private final ContentRepository contentRepository;
    private final UserAppCodeRepository userAppCodeRepository;

    @Autowired
    @Qualifier("firebaseSender")
    private MessageSender messageSender;

    @Scheduled(cron = "0 0 */1 * * *")
    public void pushMessageAboutEventDayBefore() {
        List<Content> events = contentRepository.findByEventDateGreaterThanEqualAndEventDateLessThanAndType(
                LocalDateTime.now().plusDays(1).minusMinutes(29).minusSeconds(59),
                LocalDateTime.now().plusDays(1).plusMinutes(30),
                ContentType.EVENT);

        pushMessage(events);
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void pushMessageAboutEventHourBefore() {
        List<Content> events = contentRepository.findByEventDateGreaterThanEqualAndEventDateLessThanAndType(
                LocalDateTime.now().plusHours(1).minusSeconds(149),
                LocalDateTime.now().plusHours(1).plusSeconds(150),
                ContentType.EVENT);

        pushMessage(events);
    }

    private void pushMessage(List<Content> events) {
        List<UserEntity> users;
        List<Optional<UserAppCode>> listOfCodes;
        for (Content event : events) {
            List<ContentUser> contentUsers = event.getContentUsers();
            users = contentUsers.stream().map(ContentUser::getUser).collect(Collectors.toList());
            listOfCodes = users.stream()
                    .map(user -> userAppCodeRepository.findById(String.valueOf(user.getId())))
                    .collect(Collectors.toList());

            for (Optional<UserAppCode> codes : listOfCodes) {
                for (String code : codes.get().getCodes()) {
                    messageSender.send(String.valueOf(event.getType())
                            , code
                            , event.getName()
                            , event.getId()
                            , event.getType());
                }
            }
        }
    }
}
