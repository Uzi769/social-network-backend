package com.irlix.irlixbook.dao.mapper.request.comment;

import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.comment.CommentInput;
import com.irlix.irlixbook.dao.model.post.PostInput;
import org.springframework.core.convert.converter.Converter;

public class CommentInputToComment implements Converter<CommentInput, Comment> {

    @Override
    public Comment convert(CommentInput commentInput) {
        return Comment.builder()
                .body(commentInput.getBody())
                .build();
    }
}
