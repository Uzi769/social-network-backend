package com.irlix.irlixbook.dao.mapper.chat;

import com.irlix.irlixbook.dao.entity.ChatMessage;
import com.irlix.irlixbook.dao.model.chat.response.MessageOutput;
import org.springframework.core.convert.converter.Converter;

public class ChatMessageToMessageOutput implements Converter<ChatMessage, MessageOutput> {
    @Override
    public MessageOutput convert(ChatMessage chatMessage) {
        return MessageOutput.builder()
                .localId(chatMessage.getLocalId())
                .messageId(chatMessage.getId())
                .timeStamp(chatMessage.getTimestamp())
                .content(chatMessage.getContent())
                .build();
    }
}
