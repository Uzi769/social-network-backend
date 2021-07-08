package com.irlix.irlixbook.dao.mapper.response.post;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import org.springframework.core.convert.converter.Converter;

public class PostToPostOutput implements Converter<Content, PostOutput> {

    @Override
    public PostOutput convert(Content content) {
        return PostOutput.builder()
                .id(content.getId())
//                .topic(content.getTopic())
//                .content(content.getContent())
//                .date(content.getDate())
                .build();
    }
}
