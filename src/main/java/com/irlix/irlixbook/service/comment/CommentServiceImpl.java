package com.irlix.irlixbook.service.comment;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.comment.CommentInput;
import com.irlix.irlixbook.dao.model.comment.CommentOutput;
import com.irlix.irlixbook.dao.model.comment.CommentSearch;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.CommentRepository;
import com.irlix.irlixbook.repository.summary.CommentRepositorySummary;
import com.irlix.irlixbook.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final ConversionService conversionService;
    private final CommentRepositorySummary commentRepositorySummary;

    private static final String COMMENT_NOT_FOUND = "Comment not found";

    @Override
    @Transactional
    public void save(CommentInput commentInput) {
        Comment comment = conversionService.convert(commentInput, Comment.class);
        if (comment == null) {
            log.error("CommentInput cannot be null");
            throw new NullPointerException("CommentInput cannot be null");
        }
        comment.setUser(SecurityContextUtils.getUserFromContext());
        comment.setPost(postService.getById(commentInput.getPostId()));
        commentRepository.save(comment);
        log.info("Comment saved. Class CommentServiceImpl, method save");
    }

    @Override
    public CommentOutput findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(COMMENT_NOT_FOUND);
                    return new NotFoundException(COMMENT_NOT_FOUND);
                });
        CommentOutput commentOutput = conversionService.convert(comment, CommentOutput.class);
        log.info("Return Comment by id. Class CommentServiceImpl, method findById");
        return commentOutput;
    }

    @Override
    public List<CommentOutput> findAll() {
        return commentRepository.findAll().stream()
                .map(comment -> conversionService.convert(comment, CommentOutput.class)).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
        log.info("Comment deleted. Class PostServiceImpl, method delete");
    }

    @Override
    public List<CommentOutput> search(CommentSearch dto, PageableInput pageable) {
        List<Comment> comments = commentRepositorySummary.search(dto, pageable);
        return comments.stream()
                .map(comment -> conversionService.convert(comment, CommentOutput.class))
                .collect(Collectors.toList());
    }
}
