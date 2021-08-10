package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.Sticker;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.entity.enams.PeriodType;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.ContentRepository;
import com.irlix.irlixbook.service.messaging.MessageSender;
import com.irlix.irlixbook.service.picture.PictureService;
import com.irlix.irlixbook.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
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

    @Autowired
    @Qualifier("firebaseSender")
    private MessageSender messageSender;

    @Override
    public List<ContentResponse> findImportant(ContentType type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Content> importantContent = contentRepository.findImportantContent(type, pageRequest);
        return importantContent.stream()
                .map(e -> conversionService.convert(e, ContentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContentResponse save(ContentPersistRequest contentPersistRequest) {
        Content content = conversionService.convert(contentPersistRequest, Content.class);
        if (content == null) {
            log.error("ContentPersistRequest cannot be null");
            throw new NullPointerException("ContentPersistRequest cannot be null");
        }

        if (StringUtils.hasLength(contentPersistRequest.getStickerName())) {
            Sticker sticker = stickerService.findOrCreate(contentPersistRequest.getStickerName());
            content.setSticker(sticker);
        }

        content.setCreator(SecurityContextUtils.getUserFromContext());
        content.setDateCreated(LocalDateTime.now());

        Content savedContent = contentRepository.save(content);
        savedContent.setDeeplink("/" + savedContent.getType().name() + "/" + savedContent.getId());
        contentRepository.save(savedContent);

        if (contentPersistRequest.getPicturesId() != null && !contentPersistRequest.getPicturesId().isEmpty()) {
            content.setPictures(pictureService.addContentToPicture(contentPersistRequest.getPicturesId(), savedContent));
        }
//        messageSender.send("New content was created",
//                "", //todo added logic with code of receiver
//                "New content was created");
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

    public List<ContentResponse> findByEventDateForPeriod(LocalDate start, PeriodType periodType) {
        LocalDateTime startSearch;
        LocalDateTime endSearch;
        if (PeriodType.DAY == periodType) {
            if (start == null) {
                startSearch = LocalDate.now().atStartOfDay();
                endSearch = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            } else {
                startSearch = start.atStartOfDay();
                endSearch = LocalDateTime.of(start, LocalTime.MAX);
            }
        } else if (PeriodType.WEEK == periodType) {
            if (start == null) {
                LocalDate now = LocalDate.now();
                startSearch = now.atStartOfDay();
                endSearch = LocalDateTime.of(now.plusDays(7), LocalTime.MAX);
            } else {
                startSearch = start.atStartOfDay();
                endSearch = LocalDateTime.of(start.plusDays(7), LocalTime.MAX);
            }
        } else if (PeriodType.MONTH == periodType) {
            if (start == null) {
                LocalDate now = LocalDate.now();
                startSearch = now.atStartOfDay();
                endSearch = LocalDateTime.of(now.plusDays(28), LocalTime.MAX);
            } else {
                startSearch = start.atStartOfDay();
                endSearch = LocalDateTime.of(start.plusDays(28), LocalTime.MAX);
            }
        } else {
            throw new IllegalArgumentException("PeriodType is null");
        }

        List<Content> contents = contentRepository.findByEventDateGreaterThanEqualAndEventDateLessThanAndType(startSearch, endSearch, ContentType.EVENT);
        return contents.stream()
                .map(c -> conversionService.convert(c, ContentResponse.class))
                .collect(Collectors.toList());
    }

    public Collection<String> findByEventDateForMonth(LocalDate start) {
        LocalDateTime startSearch;
        LocalDateTime endSearch;
        if (start == null) {
            startSearch = LocalDate.now().atStartOfDay();
            endSearch = LocalDateTime.of(LocalDate.now().plusDays(28), LocalTime.MAX);
        } else {
            startSearch = start.atStartOfDay();
            endSearch = LocalDateTime.of(start.plusDays(28), LocalTime.MAX);
        }
        List<Content> contents = contentRepository.findByEventDateGreaterThanEqualAndEventDateLessThanAndType(startSearch, endSearch, ContentType.EVENT);
        return contents.stream()
                .map(Content::getEventDate)
                .filter(Objects::nonNull)
                .map(LocalDateTime::toLocalDate)
                .map(date -> date.format(DateTimeFormatter.ISO_DATE))
                .collect(Collectors.toSet());
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
    public List<ContentResponse> findByType(ContentType contentType, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dateCreated").descending());
        List<Content> contents = contentRepository.findByType(contentType, pageRequest);
        return contents.stream()
                .map(post -> conversionService.convert(post, ContentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Content contentForDelete = getById(id);
        if (contentForDelete != null) {
            contentForDelete.setUsers(new ArrayList<>());
            contentRepository.save(contentForDelete);
            contentRepository.delete(contentForDelete);
            log.info("Content deleted. Class ContentServiceImpl, method delete");
        } else {
            throw new NotFoundException("Content with id nor found");
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        contentRepository.deleteAll();
        UserEntity user = SecurityContextUtils.getUserFromContext();
        log.info("DELETE ALL CONTENT BY: {}", user.getEmail());
    }

    @Override
    public List<ContentResponse> getFavorites(ContentType contentType, int page, int size) {
        UserEntity userFromContext = SecurityContextUtils.getUserFromContext();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dateCreated").descending());

        List<Content> resultContent;
        if (Objects.nonNull(contentType)) {
            resultContent = contentRepository.findByUsers_IdAndType(userFromContext.getId(), contentType, pageRequest);
        } else {
            resultContent = contentRepository.findByUsers_Id(userFromContext.getId(), pageRequest);
        }
        return resultContent.stream()
                .map(post -> conversionService.convert(post, ContentResponse.class))
                .collect(Collectors.toList());
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

        if (StringUtils.hasLength(contentPersistRequest.getStickerName())) {
            newContent.setSticker(stickerService.findOrCreate(contentPersistRequest.getStickerName()));
        }

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
