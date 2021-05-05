package com.irlix.irlixbook.service.post;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.entity.Tag;
import com.irlix.irlixbook.dao.model.post.PostInput;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.PostRepository;
import com.irlix.irlixbook.service.tag.TagService;
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
    private final TagService tagService;

    @Override
    public Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Post not found");
                    return new NotFoundException("Post not found");
                });
    }

    @Override
    public void save(PostInput postInput) {
        Post post = conversionService.convert(postInput, Post.class);
        if (post == null) {
            log.error("PostInput cannot be null");
            throw new NullPointerException("PostInput cannot be null");
        }
        post.setAuthor(SecurityContextUtils.getUserFromContext());
        List<Tag> tags = postInput.getTags().stream()
                .map(tag -> tagService.getByName(tag))
                .collect(Collectors.toList());

        post.setTags(tags);
        postRepository.save(post);
        log.info("Post saved. Class PostServiceImpl, method save");
    }

    @Override
    public PostOutput findById(Long id) {
        PostOutput postOutput = conversionService.convert(getById(id), PostOutput.class);
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
