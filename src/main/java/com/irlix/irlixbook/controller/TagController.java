package com.irlix.irlixbook.controller;

import com.irlix.irlixbook.dao.model.tag.TagInput;
import com.irlix.irlixbook.dao.model.tag.TagOutput;
import com.irlix.irlixbook.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @CrossOrigin
    @GetMapping
    public List<TagOutput> findAll() {
        return tagService.findAll();
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<TagOutput> create(@RequestBody @Valid TagInput tagInput) {
        return tagService.save(tagInput);
    }
}
