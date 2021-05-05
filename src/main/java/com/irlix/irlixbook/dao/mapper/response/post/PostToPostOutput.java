package com.irlix.irlixbook.dao.mapper.response.post;

import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.mapper.response.tag.TagToTagOutput;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostToPostOutput implements Converter<Post, PostOutput> {

    private final TagToTagOutput tagOutput;

    @Override
    public PostOutput convert(Post post) {
        return PostOutput.builder()
                .id(post.getId())
                .topic(post.getTopic())
                .userId(post.getAuthor().getId())
                .commentCount(post.getComments().size())
                .content(post.getContent())
                .date(post.getDate())
                .tagOutput(post.getTags().stream()
                        .map(tagOutput::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
