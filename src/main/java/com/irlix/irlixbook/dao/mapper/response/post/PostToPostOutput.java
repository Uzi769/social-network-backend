package com.irlix.irlixbook.dao.mapper.response.post;

import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import org.springframework.core.convert.converter.Converter;

public class PostToPostOutput implements Converter<Post, PostOutput> {

    @Override
    public PostOutput convert(Post post) {
        return PostOutput.builder()
                .id(post.getId())
                .topic(post.getTopic())
                .content(post.getContent())
                .date(post.getDate())
                .build();
    }
}