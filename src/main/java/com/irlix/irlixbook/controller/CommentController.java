package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.model.comment.CommentInput;
import com.irlix.irlixbook.dao.model.comment.CommentOutput;
import com.irlix.irlixbook.dao.model.post.PostInput;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import com.irlix.irlixbook.service.comment.CommentService;
import com.irlix.irlixbook.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @CrossOrigin
    @GetMapping
    public List<CommentOutput> findAll() {
        return commentService.findAll();
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid CommentInput commentInput) {
        commentService.save(commentInput);
    }
}
