package com.irlix.irlixbook.service.comment;

import com.irlix.irlixbook.dao.entity.Comment;
import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.comment.CommentInput;
import com.irlix.irlixbook.dao.model.comment.CommentOutput;
import com.irlix.irlixbook.repository.CommentRepository;
import com.irlix.irlixbook.service.post.PostService;
import com.irlix.irlixbook.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final ConversionService conversionService;


    @Override
    public void save(CommentInput commentInput) {
        Comment comment = conversionService.convert(commentInput, Comment.class);
        comment.setUser(userService.getById(commentInput.getUserId()));
        comment.setPost(postService.getById(commentInput.getPostId()));
        commentRepository.save(comment);
        log.info("Comment saved. Class CommentServiceImpl, method save");

    }

    @Override
    public CommentOutput findById(Long id) {
        CommentOutput commentOutput = conversionService.convert(commentRepository.findById(id), CommentOutput.class);
        log.info("Return Comment by id. Class CommentServiceImpl, method findById");
        return commentOutput;
    }

    @Override
    public List<CommentOutput> findAll() {
        List<CommentOutput> commentOutputs = commentRepository.findAll().stream()
                .map(comment -> conversionService.convert(comment, CommentOutput.class)).collect(Collectors.toList());

        return commentOutputs;
    }
}
