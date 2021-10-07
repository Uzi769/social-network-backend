package com.irlix.irlixbook.service.community;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.*;
import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.CommunityRepository;
import com.irlix.irlixbook.repository.ContentRepository;
import com.irlix.irlixbook.repository.UserContentCommunityRepository;
import com.irlix.irlixbook.repository.UserRepository;
import com.irlix.irlixbook.service.content.ContentService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{

    private final CommunityRepository communityRepository;
    private final UserContentCommunityRepository userContentCommunityRepository;
    private final ConversionService conversionService;
    private final UserService userService;
    private final ContentService contentService;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    @Value("${url.root}")
    private String urlRoot;

    @Override
    @Transactional
    public CommunityResponse save(CommunityPersistRequest communityPersistRequest) {

        Community community = conversionService.convert(communityPersistRequest, Community.class);

        if (community == null) {
            log.error("CommunityPersistRequest cannot be null.");
            throw new NullPointerException("CommunityPersistRequest cannot be null.");
        }

        //todo add sticker

        community.setCreator(SecurityContextUtils.getUserFromContext());

        Community savedCommunity = communityRepository.save(community);
        savedCommunity.setDeeplink(urlRoot + savedCommunity.getName() + "/" + savedCommunity.getId());
        savedCommunity.setRegistrationLink(urlRoot + savedCommunity.getName() + "/" + savedCommunity.getId());
        communityRepository.save(savedCommunity);

        List<UserContentCommunity> userContentCommunities = savedCommunity.getUserContentCommunities();

        if (userContentCommunities == null) {
            List<Long> contentsId = communityPersistRequest.getContentsId();
            if (contentsId == null) {
                contentsId = new ArrayList<>();
                contentsId.add(0L);
            }
            List<UUID> usersId = communityPersistRequest.getUsersId();
            if (usersId == null) {
                usersId = new ArrayList<>();
                usersId.add(new UUID(0,0));
            }
            for (Long contentId : contentsId) {
                for (UUID userId : usersId) {
                    Content content = contentRepository.findById(contentId).orElse(null);
                    UserContentCommunity userContentCommunity = UserContentCommunity.builder()
                            .community(savedCommunity)
                            .content(contentRepository.findById(contentId).orElse(null))
                            .user(userRepository.findById(userId).orElse(null))
                            .dateCreated(LocalDateTime.now())
                            .id(new UserContentCommunityId(savedCommunity.getId()
                                    , contentId
                                    , userId))
                            .build();
                    userContentCommunityRepository.save(userContentCommunity);
                    userContentCommunities.add(userContentCommunity);
                }
            }
        }

        for (UserContentCommunity userContentCommunity : userContentCommunities) {
            if (communityPersistRequest.getUsersId() != null && !communityPersistRequest.getUsersId().isEmpty()) {
                userService.addUsersToUserContentCommunity(communityPersistRequest.getUsersId(), userContentCommunity);
                userContentCommunityRepository.save(userContentCommunity);
            }
        }

        log.info("Community saved. Class CommunityServiceImpl, method save");
        return conversionService.convert(savedCommunity, CommunityResponse.class);
    }

    @Override
    public List<CommunityResponse> findAll() {
        return communityRepository.findAll().stream()
                .map(c -> conversionService.convert(c, CommunityResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommunityResponse findById(UUID uuid) {
        CommunityResponse communityResponse = conversionService.convert(getById(uuid), CommunityResponse.class);
        log.info("Return Community found by id. Class CommunityServiceImpl, method findById");
        return communityResponse;
    }

    @Override
    public CommunityResponse findByName(String name) {
        CommunityResponse communityResponse = conversionService.convert(getByName(name), CommunityResponse.class);
        log.info("Return Community found by id. Class CommunityServiceImpl, method findById");
        return communityResponse;
    }

    @Override
    public List<UserEntityOutput> findCommunityUsers(String name, int page, int size) {
        List<UserContentCommunity> userContentCommunities = getUserContentCommunities(name, page, size);
        List<UserEntity> userEntities = userContentCommunities.stream()
                .map(UserContentCommunity::getUser)
                .collect(Collectors.toList());
        return userEntities.stream()
                .map(u -> conversionService.convert(u, UserEntityOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentResponse> findCommunityContents(String name, int page, int size) {
        List<UserContentCommunity> userContentCommunities = getUserContentCommunities(name, page, size);
        List<Content> contents = userContentCommunities.stream()
                .map(UserContentCommunity::getContent)
                .collect(Collectors.toList());
        return contents.stream()
                .map(u -> conversionService.convert(u, ContentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommunityResponse addUsers(CommunityPersistRequest communityPersistRequest) {
        Community community = conversionService.convert(communityPersistRequest, Community.class);

        if (community == null) {
            log.error("CommunityPersistRequest cannot be null.");
            throw new NullPointerException("CommunityPersistRequest cannot be null.");
        }

        List<UserContentCommunity> userContentCommunities = community.getUserContentCommunities();

        for (UserContentCommunity userContentCommunity : userContentCommunities) {
            if (communityPersistRequest.getUsersId() != null && !communityPersistRequest.getUsersId().isEmpty()) {
                userService.addUsersToUserContentCommunity(communityPersistRequest.getUsersId(), userContentCommunity);
                userContentCommunityRepository.save(userContentCommunity);
            }
        }

        log.info("Users added to community. Class CommunityServiceImpl, method addUsers");
        return conversionService.convert(community, CommunityResponse.class);
    }

    @Override
    public void delete(String name) {

    }

    @Override
    public CommunityResponse addContents(CommunityPersistRequest communityPersistRequest) {
        Community community = conversionService.convert(communityPersistRequest, Community.class);

        if (community == null) {
            log.error("CommunityPersistRequest cannot be null.");
            throw new NullPointerException("CommunityPersistRequest cannot be null.");
        }

        List<UserContentCommunity> userContentCommunities = community.getUserContentCommunities();

        for (UserContentCommunity userContentCommunity : userContentCommunities) {
            if (communityPersistRequest.getUsersId() != null && !communityPersistRequest.getUsersId().isEmpty()) {
                contentService.addContentsToUserContentCommunity(communityPersistRequest.getContentsId(), userContentCommunity);
                userContentCommunityRepository.save(userContentCommunity);
            }
        }

        log.info("Contents added to community. Class CommunityServiceImpl, method addContents");
        return conversionService.convert(community, CommunityResponse.class);
    }

    private Community getById(UUID uuid) {
        return communityRepository.findById(uuid)
                .orElseThrow(() -> {
                    log.error("Community not found");
                    return new NotFoundException("Content not found");
                });
    }

    private List<Community> getByName(String name) {
        try {
            return communityRepository.findByName(name);
        } catch (IllegalArgumentException e) {
            log.error("Community not found");
            throw new NotFoundException("Content not found");
        }
    }

    private List<UserContentCommunity> getUserContentCommunities(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userContentCommunityRepository
                .findAllByCommunityName(name, pageRequest);
    }
}
