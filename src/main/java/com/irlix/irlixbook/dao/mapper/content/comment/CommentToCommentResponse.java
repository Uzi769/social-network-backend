package com.irlix.irlixbook.dao.mapper.content.comment;

import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.model.content.comment.CommentResponse;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class CommentToCommentResponse implements Converter<Comment, CommentResponse> {
    @Override
    public CommentResponse convert(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(comment.getAuthor().getName())
                .dateCreated(comment.getDateCreated())
                .contentId(comment.getContent().getId())
                .parentCommentId(comment.getParentComment().getId())
                .repliesId(comment.getReply() != null
                        ? comment.getReply().stream().map(Comment::getId).collect(Collectors.toList())
                        : null)
                .build();
    }
}
