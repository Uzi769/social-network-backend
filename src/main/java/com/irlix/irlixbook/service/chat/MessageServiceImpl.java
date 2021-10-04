package com.irlix.irlixbook.service.chat;

import com.irlix.irlixbook.dao.model.chat.request.MessageRequest;
import com.irlix.irlixbook.dao.model.chat.response.ChatOutput;
import com.irlix.irlixbook.dao.model.chat.response.MessageOutput;

import java.util.List;
import java.util.UUID;

public class MessageServiceImpl implements MessageService{
    @Override
    public List<ChatOutput> getChats() {
        return null;
    }

    @Override
    public List<MessageOutput> getLastMessages(UUID chatId, int page, int size) {
        return null;
    }

    @Override
    public MessageOutput send(UUID chatId, MessageRequest messageRequest) {
        return null;
    }

    @Override
    public MessageOutput update(UUID chatId, MessageRequest messageRequest) {
        return null;
    }
}
