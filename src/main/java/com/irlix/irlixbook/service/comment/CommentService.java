package com.irlix.irlixbook.service.comment;

import com.irlix.irlixbook.dao.model.comment.CommentInput;
import com.irlix.irlixbook.dao.model.comment.CommentOutput;

import java.util.List;

public interface CommentService {
    void save(CommentInput commentInput);

    CommentOutput findById(Long id);

    List<CommentOutput> findAll();

    void delete(Long id);
}
