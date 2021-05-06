package com.irlix.irlixbook.dao.mapper.response.post;

import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import com.irlix.irlixbook.dao.model.tag.TagOutput;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PostToPostOutput implements Converter<Post, PostOutput> {

    @Override
    public PostOutput convert(Post post) {
        return PostOutput.builder()
                .id(post.getId())
                .topic(post.getTopic())
                .userId(post.getAuthor().getId())
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                .content(post.getContent())
                .date(post.getDate())
                .tagOutput(post.getTags() != null ?
                        post.getTags().stream()
                                .map(tag -> TagOutput.builder()
                                        .name(tag.getName()).build()).collect(Collectors.toList()) :
                        new ArrayList<>())
                .build();
    }
}
