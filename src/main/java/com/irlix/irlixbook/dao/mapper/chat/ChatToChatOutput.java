package com.irlix.irlixbook.dao.mapper.chat;

import com.irlix.irlixbook.dao.entity.Chat;
import com.irlix.irlixbook.dao.model.chat.response.ChatOutput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class ChatToChatOutput implements Converter<Chat, ChatOutput> {

    private ConversionService conversionService;

    @Override
    public ChatOutput convert(Chat chat) {

        return ChatOutput.builder()
                .chatId(chat.getId())
                .title(chat.getTitle())
                .users(chat.getUsers()
                        .stream()
                        .map(user -> conversionService.convert(user, UserEntityOutput.class))
                        .collect(Collectors.toList())
                )
                .build();
    }

}
