package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.Sticker;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.ContentRepository;
import com.irlix.irlixbook.service.picture.PictureService;
import com.irlix.irlixbook.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final ConversionService conversionService;
    private final StickerService stickerService;
    private final PictureService pictureService;

    @Override
    @Transactional
    public ContentResponse save(ContentPersistRequest contentPersistRequest) {
        Content content = conversionService.convert(contentPersistRequest, Content.class);
        if (content == null) {
            log.error("ContentPersistRequest cannot be null");
            throw new NullPointerException("ContentPersistRequest cannot be null");
        }

        Sticker sticker = stickerService.findOrCreate(contentPersistRequest.getStickerName());

        content.setAuthor(SecurityContextUtils.getUserFromContext());
        content.setSticker(sticker);
        content.setDateCreated(LocalDateTime.now());

        Content savedContent = contentRepository.save(content);
        if (contentPersistRequest.getPicturesId() != null && !contentPersistRequest.getPicturesId().isEmpty()) {
            content.setPictures(pictureService.addContentToPicture(contentPersistRequest.getPicturesId(), savedContent));
        }
        log.info("Content saved. Class ContentServiceImpl, method save");
        return conversionService.convert(savedContent, ContentResponse.class);
    }

    @Override
    public ContentResponse findById(Long id) {
        ContentResponse contentResponse = conversionService.convert(getById(id), ContentResponse.class);
        log.info("Return Content found by id. Class ContentServiceImpl, method findById");
        return contentResponse;
    }

    @Override
    public Content findByIdOriginal(Long id) {
        return getById(id);
    }

    @Override
    public List<ContentResponse> findAll() {
        return contentRepository.findAll().stream()
                .map(c -> conversionService.convert(c, ContentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentResponse> search(ContentType contentType, String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dateCreated").descending());
        List<Content> contents = contentRepository.findByNameContainingIgnoreCaseAndType(name, contentType, pageRequest);
        return contents.stream()
                .map(post -> conversionService.convert(post, ContentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        contentRepository.deleteById(id);
        log.info("Content deleted. Class ContentServiceImpl, method delete");
    }

    @Override
    public ContentResponse update(Long id, @Valid ContentPersistRequest contentPersistRequest) {
        if (contentPersistRequest == null) {
            log.error("Content cannot be null, Class ContentServiceImpl, method update");
            throw new NullPointerException("Content cannot be null");
        }
        Content content = getById(id);
        if (Objects.isNull(content)) {
            log.error("Content by id: {} is absent", id);
            throw new BadRequestException("Content by id: " + id + " is absent");
        }

        Content newContent = conversionService.convert(contentPersistRequest, Content.class);
        newContent.setAuthor(content.getAuthor());
        newContent.setUsers(content.getUsers());
        newContent.setId(content.getId());
        newContent.setSticker(stickerService.findOrCreate(contentPersistRequest.getStickerName()));

        contentRepository.save(newContent);
        log.info("Update Content by id: " + id + ". Class ContentServiceImpl, method update");

        return conversionService.convert(newContent, ContentResponse.class);
    }

    private Content getById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Content not found");
                    return new NotFoundException("Content not found");
                });
    }
}
