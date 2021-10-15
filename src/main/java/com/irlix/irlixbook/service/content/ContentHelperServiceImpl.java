package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.Picture;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.entity.enams.HelperEnum;
import com.irlix.irlixbook.dao.model.content.helper.request.HelperRequest;
import com.irlix.irlixbook.dao.model.content.helper.response.HelperResponse;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class ContentHelperServiceImpl implements ContentHelperService{

    private final ConversionService conversionService;
    private final ContentRepository contentRepository;

    @Value("${url.root}")
    private String urlRoot;

    @Override
    @Transactional
    public HelperResponse save(HelperRequest helperRequest, HelperEnum helperType) {

        Content content = conversionService.convert(helperRequest, Content.class);

        if (content == null) {
            log.error("HelperRequest cannot be null");
            throw new NullPointerException("HelperRequest cannot be null");
        }

        content.setCreator(SecurityContextUtils.getUserFromContext());
        content.setDateCreated(LocalDateTime.now());
        content.setHelperType(helperType);
        content.setType(ContentType.HELPER);

        Content savedContent = contentRepository.save(content);
        savedContent.setDeeplink(urlRoot + savedContent.getType().name() + "/" + helperType + "/" + savedContent.getId());
        contentRepository.save(savedContent);

        log.info("Helper saved. Class ContentHelperServiceImpl, method save");
        return conversionService.convert(savedContent, HelperResponse.class);
    }

    @Override
    public HelperResponse update(Long id, HelperRequest helperRequest) {

        if (helperRequest == null) {
            log.error("HelperRequest cannot be null, Class ContentHelperServiceImpl, method update");
            throw new NullPointerException("HelperRequest cannot be null");
        }

        Content content = contentRepository.findById(id).orElseThrow(() -> {
            log.error("Content not found");
            return new NotFoundException("Content not found");
        });

        this.updateContent(content, helperRequest);
        contentRepository.save(content);
        log.info("Update Helper by id: " + id + ". Class ContentHelperServiceImpl, method update");

        return conversionService.convert(content, HelperResponse.class);
    }

    private void updateContent(Content content, HelperRequest newContent) {

        if (StringUtils.hasLength(newContent.getName())) {
            content.setName(newContent.getName());
        }

        if (StringUtils.hasLength(newContent.getText())) {
            content.setDescription(newContent.getText());
        }

        content.setLike(newContent.getLike());
    }



}
