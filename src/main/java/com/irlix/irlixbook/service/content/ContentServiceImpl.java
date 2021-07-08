package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.PageableInput;
import com.irlix.irlixbook.dao.model.post.PostInput;
import com.irlix.irlixbook.dao.model.post.PostOutput;
import com.irlix.irlixbook.dao.model.post.PostSearch;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.ContentRepository;
import com.irlix.irlixbook.repository.summary.ContentRepositorySummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final ConversionService conversionService;
    private final ContentRepositorySummary repositorySummary;

    @Override
    public Content getById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Post not found");
                    return new NotFoundException("Post not found");
                });
    }

    @Override
    @Transactional
    public List<PostOutput> save(PostInput postInput) {
        Content content = conversionService.convert(postInput, Content.class);
        if (content == null) {
            log.error("PostInput cannot be null");
            throw new NullPointerException("PostInput cannot be null");
        }
        content.setAuthor(SecurityContextUtils.getUserFromContext());

        contentRepository.save(content);
        log.info("Post saved. Class PostServiceImpl, method save");
        return findAll();
    }

    @Override
    public PostOutput findById(Long id) {
        PostOutput postOutput = conversionService.convert(getById(id), PostOutput.class);
        log.info("Return Post found by id. Class PostServiceImpl, method findById");
        return postOutput;
    }

    @Override
    public List<PostOutput> findAll() {
        return contentRepository.findAll().stream()
                .map(post -> conversionService.convert(post, PostOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostOutput> search(PostSearch dto, PageableInput pageable) {
        List<Content> contents = repositorySummary.search(dto, pageable);
        return contents.stream()
                .map(post -> conversionService.convert(post, PostOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostOutput>  delete(Long id) {
       contentRepository.deleteById(id);

        log.info("Post deleted. Class PostServiceImpl, method delete");

        return findAll();
    }

    @Override
    public List<PostOutput>  update(Long id, @Valid PostInput postInput) {
        Content content = getById(id);
        if (postInput == null) {
            log.error("Post cannot be null, Class PostServiceImpl, method update");
            throw new NullPointerException("Post cannot be null");
        }
        Content newContent = conversionService.convert(postInput, Content.class);

        updatePost(content, newContent);
        contentRepository.save(content);
        log.info("Update office by id: " + id + ". Class OfficeServiceImpl, method update");

        return findAll();
    }
    private void updatePost(Content content, Content newContent) {
//        if (newContent.getTopic() != null) {
//            content.setTopic(newContent.getTopic());
//        }
//        if (newContent.getContent() != null) {
//            content.setContent(newContent.getContent());
//        }
    }

}
