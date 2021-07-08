package com.irlix.irlixbook.dao.mapper.request.post;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.post.PostInput;
import org.springframework.core.convert.converter.Converter;

public class PostInputToPost implements Converter<PostInput, Content> {

    @Override
    public Content convert(PostInput postInput) {
        return Content.builder()
//                .topic(postInput.getTopic())
//                .content(postInput.getContent())
                .build();
    }
}
