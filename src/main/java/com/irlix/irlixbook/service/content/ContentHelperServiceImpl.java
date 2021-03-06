package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.dao.entity.enams.ContentTypeEnum;
import com.irlix.irlixbook.dao.entity.enams.HelperEnum;
import com.irlix.irlixbook.dao.model.content.helper.request.HelperRequest;
import com.irlix.irlixbook.dao.model.content.helper.request.HelperSearchRequest;
import com.irlix.irlixbook.dao.model.content.helper.response.HelperResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.ForbiddenException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ContentHelperServiceImpl implements ContentHelperService{

    private final ConversionService conversionService;
    private final ContentRepository contentRepository;
    private final ContentService contentService;

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
        content.setType(ContentTypeEnum.HELPER);
        content.setLike(0);

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

        Content content = getById(id);

        this.updateContent(content, helperRequest);
        contentRepository.save(content);
        log.info("Update Helper by id: " + id + ". Class ContentHelperServiceImpl, method update");

        return conversionService.convert(content, HelperResponse.class);
    }

    @Override
    public HelperResponse findById(Long id) {

        Content content = getById(id);

        if (content.getType() != ContentTypeEnum.HELPER) {
            log.error("This content belongs not for a helper.");
            throw new BadRequestException("This content belongs not for a helper.");
        }

        return conversionService.convert(content, HelperResponse.class);

    }

    @Override
    public List<HelperResponse> findHelpers(HelperEnum helperType, HelperSearchRequest helperRequest, int page, int size) {

        User userFromContext = SecurityContextUtils.getUserFromContext();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dateCreated").descending());

        List<Content> resultContent = new ArrayList<>();

        if (helperRequest.isShowMyHelpers()) {
            resultContent = contentRepository.findByTypeAndHelperTypeAndCreator(ContentTypeEnum.HELPER,
                    helperType,
                    userFromContext,
                    pageRequest);
            log.info("Found all helper's of current user.");
        } else if (helperRequest.isShowTodayHelpers()) {//todo need to check date
            LocalDateTime start = LocalDate.now().atStartOfDay();
            LocalDateTime end = LocalDate.now().plusDays(1L).atStartOfDay().minusSeconds(1L);
            resultContent = contentRepository.findByTypeAndAndHelperTypeAndDateCreatedBetween(ContentTypeEnum.HELPER,
                    helperType,
                    start,
                    end,
                    pageRequest);
            log.info("Found all today helper's.");
        }

        return resultContent.stream()
                .map(post -> conversionService.convert(post, HelperResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HelperResponse> findAllHelpers(HelperEnum helperType, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dateCreated").descending());
        List<Content> resultContent = contentRepository.findByTypeAndHelperType(ContentTypeEnum.HELPER,
                helperType,
                pageRequest);
        log.info("Found all helper's of type " + helperType + ".");
        return resultContent.stream()
                .map(content -> conversionService.convert(content, HelperResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHelper(Long id) {
        Content content = getById(id);
        if (content.getType() != ContentTypeEnum.HELPER) {
            throw new BadRequestException("In this request you can delete only helper's.");
        }
        UUID currentUser = SecurityContextUtils.getUserFromContext().getId();
        UUID creatorOfHelper = content.getCreator().getId();
        if (currentUser.equals(creatorOfHelper)) {
            contentService.delete(id);
        } else {
            throw new ForbiddenException("You have no permissions for deleting this helper");
        }
    }

    private void updateContent(Content content, HelperRequest newContent) {

        if (StringUtils.hasLength(newContent.getTitle())) {
            content.setName(newContent.getTitle());
        }

        if (StringUtils.hasLength(newContent.getDescription())) {
            content.setDescription(newContent.getDescription());
        }
    }

    private Content getById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Helper not found");
                    return new NotFoundException("Helper not found");
                });
    }
}
