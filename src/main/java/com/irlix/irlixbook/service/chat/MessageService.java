package com.irlix.irlixbook.service.chat;

import com.irlix.irlixbook.dao.model.chat.request.MessageRequest;
import com.irlix.irlixbook.dao.model.chat.response.ChatOutput;
import com.irlix.irlixbook.dao.model.chat.response.MessageOutput;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<ChatOutput> getChats();

    List<MessageOutput> getLastMessages(UUID chatId, int page, int size);

    MessageOutput send(UUID chatId, MessageRequest messageRequest);

    MessageOutput update(UUID chatId, MessageRequest messageRequest);
}
