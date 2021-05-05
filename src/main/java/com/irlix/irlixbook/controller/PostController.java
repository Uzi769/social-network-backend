package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.post.PostSearch;
import com.irlix.irlixbook.dao.model.user.UserInputSearch;
import com.irlix.irlixbook.service.post.PostService;
import com.irlix.irlixbook.dao.model.post.PostInput;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @CrossOrigin
    @GetMapping
    public List<PostOutput> findAll() {
        return postService.findAll();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    public PostOutput findById(@PathVariable("id") Long id) {
        return postService.findById(id);
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid PostInput postInput) {
        postService.save(postInput);
    }

    @CrossOrigin
    @GetMapping(value = "/search")
    public List<PostOutput> search(PostSearch dto, PageableInput pageable) {
        return postService.search(dto, pageable);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}}")
    public void delete(@PathVariable("id") Long id) {
        postService.delete(id);
    }

}
