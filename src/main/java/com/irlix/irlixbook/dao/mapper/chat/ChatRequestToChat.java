package com.irlix.irlixbook.dao.mapper.chat;

import com.irlix.irlixbook.dao.entity.Chat;
import com.irlix.irlixbook.dao.model.chat.request.ChatRequest;
import org.springframework.core.convert.converter.Converter;

public class ChatRequestToChat implements Converter<ChatRequest, Chat> {

    @Override
    public Chat convert(ChatRequest chatRequest) {

        return Chat.builder()
                .title(chatRequest.getTitle())
                .users(chatRequest.getUsers())
                .build();
    }

}
