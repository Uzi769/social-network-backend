package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.*;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.entity.enams.PeriodType;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.ConflictException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.ContentRepository;
import com.irlix.irlixbook.repository.ContentUserRepository;
import com.irlix.irlixbook.repository.UserAppCodeRepository;
import com.irlix.irlixbook.service.messaging.MessageSender;
import com.irlix.irlixbook.service.picture.PictureService;
import com.irlix.irlixbook.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.irlix.irlixbook.utils.Consts.CONTENT_NOT_FOUND;

@Log4j2
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final ConversionService conversionService;
    private final StickerService stickerService;
    private final PictureService pictureService;
    private final ContentUserRepository contentUserRepository;
    private final UserAppCodeRepository userAppCodeRepository;

    @Autowired
    @Qualifier("firebaseSender")
    private MessageSender messageSender;

    @Value("${url.root}")
    private String urlRoot;

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
        savedContent.setDeeplink(urlRoot + savedContent.getType().name() + "/" + savedContent.getId());
        contentRepository.save(savedContent);

        if (contentPersistRequest.getPicturesId() != null && !contentPersistRequest.getPicturesId().isEmpty()) {
            content.setPictures(pictureService.addContentToPicture(contentPersistRequest.getPicturesId(), savedContent));
        }

//        messageSender.send("New content was created",
//                "", //todo added logic with code of receiver
//                "New content was created");

        if (savedContent.getType() == ContentType.NEWS || savedContent.getType() == ContentType.EVENT) {

            List<UserAppCode> listOfCodes = userAppCodeRepository.findAll();

            for (UserAppCode codes : listOfCodes) {
                for (String code : codes.getCodes()) {
                    messageSender.send(savedContent.getType() + " was created."
                            , code
                            , savedContent.getName()
                            , savedContent.getId(), savedContent.getType());
                }
            }

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
        return contentRepository.findAll()
                .stream()
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
        PageRequest pageRequest;

        if (Objects.nonNull(contentType) && contentType == ContentType.NEWS) {
            pageRequest = PageRequest.of(page, size, Sort.by("createdOn").descending());
        } else {
            pageRequest = PageRequest.of(page, size);
        }

        List<Content> resultContent;

        if (Objects.nonNull(contentType)) {

            List<ContentUser> favorites = contentUserRepository.findByUserIdAndContent_Type(userFromContext.getId(), contentType, pageRequest);
            resultContent = favorites.stream().map(ContentUser::getContent).collect(Collectors.toList());

        } else {

            List<ContentUser> favorites = contentUserRepository.findByUserId(userFromContext.getId(), pageRequest);
            resultContent = favorites.stream().map(ContentUser::getContent).collect(Collectors.toList());

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

        this.updateContent(content, contentPersistRequest);
        contentRepository.save(content);
        log.info("Update Content by id: " + id + ". Class ContentServiceImpl, method update");

        return conversionService.convert(content, ContentResponse.class);

    }

    private void updateContent(Content content, ContentPersistRequest newContent) {

        if (newContent.getEventDate() != null) {
            content.setEventDate(newContent.getEventDate());
        }
        if (StringUtils.hasLength(newContent.getName())) {
            content.setName(newContent.getName());
        }
        if (StringUtils.hasLength(newContent.getType())) {
            content.setType(ContentType.valueOf(newContent.getType()));
        }
        if (StringUtils.hasLength(newContent.getDescription())) {
            content.setDescription(newContent.getDescription());
        }
        if (StringUtils.hasLength(newContent.getShortDescription())) {
            content.setShortDescription(newContent.getShortDescription());
        }
        if (StringUtils.hasLength(newContent.getRegistrationLink())) {
            content.setRegistrationLink(newContent.getRegistrationLink());
        }
        if (StringUtils.hasLength(newContent.getStickerName())) {
            content.setSticker(stickerService.findOrCreate(newContent.getStickerName()));
        }
        if (StringUtils.hasLength(newContent.getAuthor())) {
            content.setAuthor(newContent.getAuthor());
        }
        if (!CollectionUtils.isEmpty(newContent.getPicturesId())) {
            List<Picture> newPictures = pictureService.addContentToPicture(newContent.getPicturesId(), content);
            content.setPictures(newPictures);
        }

    }

    private Content getById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Content not found");
                    return new NotFoundException("Content not found");
                });
    }

    @Override
    public List<Content> addContentsToUserContentCommunity(List<Long> contentsIdList,
                                                           ContentCommunity contentCommunity) {
        List<Content> contents = new ArrayList<>();
        contentsIdList.stream()
                .map(id -> contentRepository.findById(id).orElseThrow(() -> {
                    log.error(CONTENT_NOT_FOUND);
                    return new ConflictException(CONTENT_NOT_FOUND);
                }))
                .forEach(content -> {
                    content.getContentCommunities().add(contentCommunity);
                    Content savedContent = contentRepository.save(content);
                    contents.add(savedContent);
                });
        return contents;
    }
    }
