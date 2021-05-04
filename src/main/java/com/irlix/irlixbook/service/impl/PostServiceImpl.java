package com.irlix.irlixbook.service.impl;

import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.model.PostInput;
import com.irlix.irlixbook.dao.model.PostOutput;
import com.irlix.irlixbook.repository.PostRepository;
import com.irlix.irlixbook.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ConversionService conversionService;


    @Override
    public void save(PostInput postInput) {
        Post post = conversionService.convert(postInput, Post.class);
        postRepository.save(post);
        log.info("Post saved. Class PostServiceImpl, method save");
    }

    @Override
    public PostOutput findById(Long id) {
        PostOutput postOutput = conversionService.convert(postRepository.findById(id), PostOutput.class);
        log.info("Return Post found by id. Class PostServiceImpl, method findById");
        return postOutput;
    }

    @Override
    public List<PostOutput> findAll() {
        List<PostOutput> postOutputs = postRepository.findAll().stream()
                .map(post -> conversionService.convert(post, PostOutput.class)).collect(Collectors.toList());

        return postOutputs;
    }
}