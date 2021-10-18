package com.irlix.irlixbook.dao.mapper.content.comment;

import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.model.content.comment.CommentRequest;
import org.springframework.core.convert.converter.Converter;

public class CommentRequestToComment implements Converter<CommentRequest, Comment>{

    @Override
    public Comment convert(CommentRequest commentRequest) {
        return Comment.builder()
                .text(commentRequest.getText())
                .build();
    }
}
