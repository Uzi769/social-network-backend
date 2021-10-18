package com.irlix.irlixbook.service.comment;

import com.irlix.irlixbook.dao.model.content.comment.CommentRequest;
import com.irlix.irlixbook.dao.model.content.comment.CommentResponse;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {
    CommentResponse save(Long helperId, CommentRequest commentRequest);

    @Transactional
    void delete(Long id);
}
