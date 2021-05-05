package com.irlix.irlixbook.dao.mapper.response.comment;

import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.comment.CommentOutput;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import org.springframework.core.convert.converter.Converter;

public class CommentToCommentOutput implements Converter<Comment, CommentOutput> {

    @Override
    public CommentOutput convert(Comment comment) {
        return CommentOutput.builder()
                .body(comment.getBody())
                .date(comment.getDate())
                .user(comment.getUser())
                .post(comment.getPost())
                .build();
    }
}
