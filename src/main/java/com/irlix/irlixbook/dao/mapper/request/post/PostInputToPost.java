package com.irlix.irlixbook.dao.mapper.request.post;

import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.post.PostInput;
import org.springframework.core.convert.converter.Converter;

public class PostInputToPost implements Converter<PostInput, Post> {

    @Override
    public Post convert(PostInput postInput) {
        return Post.builder()
                .topic(postInput.getTopic())
                .content(postInput.getContent())
                .build();
    }
}
