package com.irlix.irlixbook.service.comment;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.model.content.comment.CommentRequest;
import com.irlix.irlixbook.dao.model.content.comment.CommentResponse;
import com.irlix.irlixbook.dao.model.content.helper.response.HelperResponse;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.CommentRepository;
import com.irlix.irlixbook.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final ConversionService conversionService;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentResponse save(Long helperId, CommentRequest commentRequest) {
        Comment comment = conversionService.convert(commentRequest, Comment.class);

        if (comment == null) {
            log.error("CommentRequest cannot be null");
            throw new NullPointerException("CommentRequest cannot be null");
        }

        comment.setAuthor(SecurityContextUtils.getUserFromContext());
        comment.setContent(contentRepository.findById(helperId).orElseThrow(() -> {
            log.error("Content not found");
            return new NotFoundException("Content not found");
        }));
        comment.setParentComment(commentRepository.findById(commentRequest.getParentCommentId()).orElseThrow(() -> {
            log.error("Comment not found");
            return new NotFoundException("Comment not found");
        }));

        commentRepository.save(comment);

        log.info("Comment saved. Class CommentServiceImpl, method save");
        return conversionService.convert(comment, CommentResponse.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Comment commentForDelete = getById(id);

        commentRepository.save(commentForDelete);
        commentRepository.delete(commentForDelete);
        log.info("Comment deleted. Class CommentServiceImpl, method delete");
    }

    private Comment getById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment with id " + id + " not found");
                    return new NotFoundException("Comment with id " + id + " not found");
                });
    }
}
