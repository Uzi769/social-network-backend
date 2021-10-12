package com.irlix.irlixbook.dao.mapper.chat;

import com.irlix.irlixbook.dao.entity.ChatMessage;
import com.irlix.irlixbook.dao.model.chat.request.LocalMessageRequest;
import org.springframework.core.convert.converter.Converter;

public class LocalMessageRequestToChatMessage implements Converter<LocalMessageRequest, ChatMessage> {

    @Override
    public ChatMessage convert(LocalMessageRequest localMessageRequest) {
        return ChatMessage.builder()
                .localId(localMessageRequest.getLocalId())
                .timestamp(localMessageRequest.getTimeStamp())
                .content(localMessageRequest.getContent())
                .build();
    }

}
