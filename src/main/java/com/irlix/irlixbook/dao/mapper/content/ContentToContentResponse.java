package com.irlix.irlixbook.dao.mapper.content;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class ContentToContentResponse implements Converter<Content, ContentResponse> {

    @Override
    public ContentResponse convert(Content content) {
        return ContentResponse.builder()
                .id(content.getId())
                .author(content.getAuthor().getId().toString())
                .description(content.getDescription())
                .shortDescription(content.getShortDescription())
                .name(content.getName())
                .registrationLink(content.getRegistrationLink())
                .type(content.getType().name())
                .stickerName(content.getSticker() != null ? content.getSticker().getName() : null)
                .users(content.getUsers() != null
                        ? content.getUsers().stream().map(u -> u.getId().toString()).collect(Collectors.toList())
                        : null)
                .pictures(content.getPictures() != null
                        ? content.getPictures().stream().map(p -> p.getUrl()).collect(Collectors.toList())
                        : null)
                .build();
    }
}
