package com.irlix.irlixbook.service.content;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.*;
import com.irlix.irlixbook.dao.entity.enams.ContentTypeEnum;
import com.irlix.irlixbook.dao.entity.enams.PeriodTypeEnum;
import com.irlix.irlixbook.dao.helper.*;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.ConflictException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.ContentCommunityRepository;
import com.irlix.irlixbook.repository.ContentRepository;
import com.irlix.irlixbook.repository.ContentUserRepository;
import com.irlix.irlixbook.repository.UserAppCodeRepository;
import com.irlix.irlixbook.service.messaging.MessageSender;
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
    private final StickerHelper stickerHelper;
    private final PictureHelper pictureHelper;
    private final ContentUserRepository contentUserRepository;
    private final UserAppCodeRepository userAppCodeRepository;
    private final ContentCommunityRepository contentCommunityRepository;

    @Autowired
    @Qualifier("firebaseSender")
    private MessageSender messageSender;

    @Value("${url.root}")
    private String urlRoot;

    @Override
    public List<ContentResponse> findImportant(ContentTypeEnum type, int page, int size) {

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
            Sticker sticker = stickerHelper.findOrCreate(contentPersistRequest.getStickerName());
            content.setSticker(sticker);
        }

        content.setCreator(SecurityContextUtils.getUserFromContext());
        content.setDateCreated(LocalDateTime.now());

        Content savedContent = contentRepository.save(content);
        savedContent.setDeeplink(urlRoot + savedContent.getType().name() + "/" + savedContent.getId());
        contentRepository.save(savedContent);

        if (contentPersistRequest.getPicturesId() != null && !contentPersistRequest.getPicturesId().isEmpty()) {
            content.setPictures(pictureHelper.addContentToPicture(contentPersistRequest.getPicturesId(), savedContent));
        }

//        messageSender.send("New content was created",
//                "", //todo added logic with code of receiver
//                "New content was created");

        if (savedContent.getType() == ContentTypeEnum.NEWS || savedContent.getType() == ContentTypeEnum.EVENT) {

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
    @Transactional(readOnly = true)
    public ContentResponse findById(Long id) {

        ContentResponse contentResponse = conversionService.convert(getById(id), ContentResponse.class);
        log.info("Return Content found by id. Class ContentServiceImpl, method findById");
        return contentResponse;

    }

    @Override
    @Transactional(readOnly = true)
    public Content findByIdOriginal(Long id) {
        return getById(id);
    }

    @Transactional(readOnly = true)
    public List<ContentResponse> findByEventDateForPeriod(LocalDate start, PeriodTypeEnum periodTypeEnum) {

        LocalDateTime startSearch;
        LocalDateTime endSearch;

        if (PeriodTypeEnum.DAY == periodTypeEnum) {

            if (start == null) {
                startSearch = LocalDate.now().atStartOfDay();
                endSearch = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            } else {
                startSearch = start.atStartOfDay();
                endSearch = LocalDateTime.of(start, LocalTime.MAX);
            }

        } else if (PeriodTypeEnum.WEEK == periodTypeEnum) {

            if (start == null) {
                LocalDate now = LocalDate.now();
                startSearch = now.atStartOfDay();
                endSearch = LocalDateTime.of(now.plusDays(7), LocalTime.MAX);
            } else {
                startSearch = start.atStartOfDay();
                endSearch = LocalDateTime.of(start.plusDays(7), LocalTime.MAX);
            }

        } else if (PeriodTypeEnum.MONTH == periodTypeEnum) {

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

        List<Content> contents = contentRepository.findByEventDateGreaterThanEqualAndEventDateLessThanAndType(startSearch, endSearch, ContentTypeEnum.EVENT);
        return contents.stream()
                .map(c -> conversionService.convert(c, ContentResponse.class))
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
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

        List<Content> contents = contentRepository.findByEventDateGreaterThanEqualAndEventDateLessThanAndType(startSearch, endSearch, ContentTypeEnum.EVENT);
        return contents.stream()
                .map(Content::getEventDate)
                .filter(Objects::nonNull)
                .map(LocalDateTime::toLocalDate)
                .map(date -> date.format(DateTimeFormatter.ISO_DATE))
                .collect(Collectors.toSet());

    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentResponse> findAll() {
        return contentRepository.findAll()
                .stream()
                .map(c -> conversionService.convert(c, ContentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentResponse> search(ContentTypeEnum contentTypeEnum, String name, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dateCreated").descending());
        List<Content> contents = contentRepository.findByNameContainingIgnoreCaseAndType(name, contentTypeEnum, pageRequest);

        return contents.stream()
                .map(post -> conversionService.convert(post, ContentResponse.class))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public List<ContentResponse> findByType(ContentTypeEnum contentTypeEnum, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dateCreated").descending());
        List<Content> contents = contentRepository.findByType(contentTypeEnum, pageRequest);

        return contents.stream()
                .map(post -> {
                    contentRepository.save(post);
                    return conversionService.convert(post, ContentResponse.class);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Content contentForDelete = getById(id);
        Collection<Content> forDelete = new HashSet<>();
        forDelete.add(contentForDelete);

        if (contentForDelete != null) {

            contentRepository.save(contentForDelete);
            deleteContentCommunities(contentForDelete);
            contentRepository.deleteInBatch(forDelete);
            contentRepository.flush();
            log.info("Content deleted. Class ContentServiceImpl, method delete");

        } else {
            throw new NotFoundException("Content with id not found");
        }

    }

    @Override
    @Transactional
    public void deleteAll() {

        contentRepository.deleteAll();
        User user = SecurityContextUtils.getUserFromContext();
        log.info("DELETE ALL CONTENT BY: {}", user.getEmail());

    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentResponse> getFavorites(ContentTypeEnum contentTypeEnum, int page, int size) {

        User userFromContext = SecurityContextUtils.getUserFromContext();
        PageRequest pageRequest;

        if (Objects.nonNull(contentTypeEnum) && contentTypeEnum == ContentTypeEnum.NEWS) {
            pageRequest = PageRequest.of(page, size, Sort.by("createdOn").descending());
        } else {
            pageRequest = PageRequest.of(page, size);
        }

        List<Content> resultContent;

        if (Objects.nonNull(contentTypeEnum)) {

            List<ContentUser> favorites = contentUserRepository.findByUserIdAndContent_Type(userFromContext.getId(), contentTypeEnum, pageRequest);
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
    @Transactional
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
            content.setType(ContentTypeEnum.valueOf(newContent.getType()));
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
            content.setSticker(stickerHelper.findOrCreate(newContent.getStickerName()));
        }
        if (StringUtils.hasLength(newContent.getAuthor())) {
            content.setAuthor(newContent.getAuthor());
        }
        if (!CollectionUtils.isEmpty(newContent.getPicturesId())) {
            List<Picture> newPictures = pictureHelper.addContentToPicture(newContent.getPicturesId(), content);
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
    @Transactional
    public List<ContentCommunity> addContentsToContentCommunity(List<Long> contentsIdList,
                                                       Community community) {
        List<ContentCommunity> contentCommunities = new ArrayList<>();
        List<Long> allContentsOfCommunity = contentCommunityRepository.findByCommunityName(community.getName()).stream()
                .map(r -> r.getContent().getId()).collect(Collectors.toList());
        contentsIdList.removeAll(allContentsOfCommunity);
        if (contentsIdList.size() != 0) {
            contentsIdList.stream()
                    .map(id -> contentRepository.findById(id).orElseThrow(() -> {
                        log.error(CONTENT_NOT_FOUND);
                        return new ConflictException(CONTENT_NOT_FOUND);
                    }))
                    .forEach(content -> {
                        ContentCommunity contentCommunity =ContentCommunity.builder()
                                .community(community)
                                .content(content)
                                .id(new ContentCommunityId(community.getId(), content.getId()))
                                .build();
                        contentCommunityRepository.save(contentCommunity);
                        contentRepository.save(content);
                        contentCommunities.add(contentCommunity);
                    });
        } else {
            log.error("All these contents are exist in community.");
            throw new BadRequestException("All these contents are exist in community.");
        }
        return contentCommunities;
    }

    private void deleteContentCommunities(Content content) {
        List<ContentCommunity> forDelete = contentCommunityRepository.findByContent(content);
        contentCommunityRepository.deleteInBatch(forDelete);
    }

}
