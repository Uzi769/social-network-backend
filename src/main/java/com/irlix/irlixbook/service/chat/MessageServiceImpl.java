package com.irlix.irlixbook.service.chat;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.ChatMessage;
import com.irlix.irlixbook.dao.model.chat.request.LocalMessageRequest;
import com.irlix.irlixbook.dao.model.chat.request.MessageRequest;
import com.irlix.irlixbook.dao.model.chat.response.ChatOutput;
import com.irlix.irlixbook.dao.model.chat.response.MessageOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ConversionService conversionService;

    @Override
    public List<ChatOutput> getChats() {
        return messageRepository.findAll()
                .stream()
                .map(c -> conversionService.convert(c, ChatOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageOutput> getLastMessages(UUID chatId, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());

        return messageRepository.findByChat(chatId, pageRequest)
                .stream()
                .map(c -> conversionService.convert(c, MessageOutput.class))
                .collect(Collectors.toList());

    }

    @Override
    public MessageOutput send(UUID chatId, LocalMessageRequest localMessageRequest) {

        ChatMessage chatMessage = conversionService.convert(localMessageRequest, ChatMessage.class);

        if (chatMessage == null) {
            log.error("LocalMessageRequest cannot be null");
            throw new NullPointerException("LocalMessageRequest cannot be null");
        }

        chatMessage.setSender(SecurityContextUtils.getUserFromContext());
        chatMessage.setTimestamp(LocalDateTime.now());

        messageRepository.save(chatMessage);
        log.info("Message sent");

        return conversionService.convert(chatMessage, MessageOutput.class);

    }

    @Override
    public MessageOutput update(UUID chatId, MessageRequest messageRequest) {

        ChatMessage chatMessage = messageRepository.findById(messageRequest.getId())
                .orElseThrow(() -> {
                    log.error("Message not found");
                    return new NotFoundException("Message not found");
                });

        chatMessage.setContent(messageRequest.getContent());

        messageRepository.save(chatMessage);
        log.info("Message [" + chatMessage.getId() + "] from chat [" + chatId + "] was updated");

        return conversionService.convert(chatMessage, MessageOutput.class);

    }

}
