package com.irlix.irlixbook.dao.mapper.content.helper;

import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.content.helper.response.HelperResponse;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class ContentToHelperResponse implements Converter<Content, HelperResponse> {
    @Override
    public HelperResponse convert(Content content) {
        return HelperResponse.builder()
                .name(content.getName())
                .helperType(content.getHelperType())
                .creator(content.getCreator().getName())
                .text(content.getDescription())
                .deepLink(content.getDeeplink())
                .creatingDate(content.getDateCreated())
                .comments(content
                        .getComments()
                        .stream()
                        .map(Comment::getId)
                        .collect(Collectors.toList()))
                .like(content.getLike())
                .build();
    }
}
