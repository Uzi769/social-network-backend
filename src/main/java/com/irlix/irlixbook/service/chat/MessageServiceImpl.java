package com.irlix.irlixbook.service.chat;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Chat;
import com.irlix.irlixbook.dao.entity.ChatMessage;
import com.irlix.irlixbook.dao.model.chat.request.ChatRequest;
import com.irlix.irlixbook.dao.model.chat.request.LocalMessageRequest;
import com.irlix.irlixbook.dao.model.chat.request.MessageRequest;
import com.irlix.irlixbook.dao.model.chat.response.ChatOutput;
import com.irlix.irlixbook.dao.model.chat.response.MessageOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.ChatRepository;
import com.irlix.irlixbook.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ConversionService conversionService;

    // ================================================================================ CHAT METHODS

    @Override
    public ChatOutput createChat(ChatRequest chatRequest) {

        Chat chat = conversionService.convert(chatRequest, Chat.class);

        if (chat == null) {
            log.error("ChatRequest cannot be null");
            throw new NullPointerException("ChatRequest cannot be null");
        }

        chat.setTitle(chatRequest.getTitle());
        chat.setUsers(chatRequest.getUsers());

        chatRepository.save(chat);
        log.info("Chat created");

        return conversionService.convert(chat, ChatOutput.class);

    }

    @Override
    @Transactional
    public void deleteChat(UUID chatId) {

        Chat chatForDelete = chatRepository.getById(chatId);

        if (chatForDelete != null) {

            chatRepository.save(chatForDelete);
            chatRepository.delete(chatForDelete);
            log.info("Chat deleted");

        } else {
            throw new NotFoundException("Chat with " + chatId + " id not found");
        }

    }

    @Override
    public List<ChatOutput> getChats() {
        return messageRepository.findAll()
                .stream()
                .map(c -> conversionService.convert(c, ChatOutput.class))
                .collect(Collectors.toList());
    }

    // ================================================================================ MESSAGES METHODS

    @Override
    public List<MessageOutput> getLastMessages(UUID chatId, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());

        return messageRepository.findByChatId(chatId, pageRequest)
                .stream()
                .map(message -> conversionService.convert(message, MessageOutput.class))
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
        chatMessage.setTimestamp(localMessageRequest.getTimeStamp());
        chatMessage.setChat(chatRepository.getById(chatId));

        messageRepository.save(chatMessage);
        log.info("Message sent");

        return conversionService.convert(chatMessage, MessageOutput.class);

    }

    @Override
    public MessageOutput update(MessageRequest messageRequest) {

        ChatMessage chatMessage = messageRepository.findById(messageRequest.getId())
                .orElseThrow(() -> {
                    log.error("Message not found");
                    return new NotFoundException("Message not found");
                });

        chatMessage.setContent(messageRequest.getContent());

        messageRepository.save(chatMessage);
        log.info("Message [" + chatMessage.getId() + "] was updated");

        return conversionService.convert(chatMessage, MessageOutput.class);

    }

}
