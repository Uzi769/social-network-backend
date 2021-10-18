package com.irlix.irlixbook.dao.mapper.content.helper;

import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.content.helper.response.HelperResponse;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class ContentToHelperResponse implements Converter<Content, HelperResponse> {
    @Override
    public HelperResponse convert(Content content) {

        List<Comment> comments = content.getComments();

        return HelperResponse.builder()
                .id(content.getId())
                .title(content.getName())
                .helperType(content.getHelperType())
                .author(content.getCreator().getName())
                .description(content.getDescription())
                .deepLink(content.getDeeplink())
                .creatingDate(content.getDateCreated())
                .comments(comments != null ? comments
                        .stream()
                        .map(Comment::getId)
                        .collect(Collectors.toList()) : null)
                .like(content.getLike())
                .avatar(content.getCreator().getAvatar())
                .build();
    }
}
