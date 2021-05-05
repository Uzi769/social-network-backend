package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.service.post.PostService;
import com.irlix.irlixbook.dao.model.PostInput;
import com.irlix.irlixbook.dao.model.PostOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostOutput> findAll() {
        return postService.findAll();
    }

    @GetMapping(value = "/{id}")
    public PostOutput findById(@PathVariable("id") Long id) {
        return postService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid PostInput postInput) {
        postService.save(postInput);
    }


}
