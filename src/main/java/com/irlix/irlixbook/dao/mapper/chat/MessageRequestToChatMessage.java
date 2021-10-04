package com.irlix.irlixbook.dao.mapper.chat;

import com.irlix.irlixbook.dao.entity.ChatMessage;
import com.irlix.irlixbook.dao.model.chat.request.MessageRequest;
import org.springframework.core.convert.converter.Converter;

public class MessageRequestToChatMessage implements Converter<MessageRequest, ChatMessage> {
    @Override
    public ChatMessage convert(MessageRequest messageRequest) {
        return ChatMessage.builder()
                .localId(messageRequest.getLocalId())
                .timestamp(messageRequest.getTimeStamp())
                .content(messageRequest.getContent())
                .sender(messageRequest.getUserId())
                .build();
    }
}
