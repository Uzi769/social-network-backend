package com.irlix.irlixbook.service.community;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.*;
import com.irlix.irlixbook.dao.entity.enams.StatusEnum;
import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.*;
import com.irlix.irlixbook.service.content.ContentService;
import com.irlix.irlixbook.service.user.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{

    private final CommunityRepository communityRepository;
    private final ContentCommunityRepository contentCommunityRepository;
    private final ConversionService conversionService;
    private final ContentService contentService;
    private final ContentRepository contentRepository;
    private final UserService userService;
    private final StatusRepository statusRepository;
    private final RoleStatusUserCommunityRepository roleStatusUserCommunityRepository;

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

        UserEntity userFromContext = SecurityContextUtils.getUserFromContext();
        community.setCreator(userFromContext);

        Community savedCommunity = communityRepository.save(community);
        savedCommunity.setDeeplink(urlRoot + savedCommunity.getName() + "/" + savedCommunity.getId());
        savedCommunity.setRegistrationLink(urlRoot + savedCommunity.getName() + "/" + savedCommunity.getId());
        communityRepository.save(savedCommunity);

        if (communityPersistRequest.getContentsId() != null) {
            contentService.addContentsToContentCommunity(communityPersistRequest.getContentsId(), savedCommunity);
        }

        RoleStatusUserCommunity roleStatusUserCommunity = RoleStatusUserCommunity.builder()
                .role(userFromContext.getRole())
                .status(statusRepository.findByName(StatusEnum.COMMUNITY_LEADER))
                .user(userFromContext)
                .community(savedCommunity)
                .Id(new RoleStatusUserCommunityId(
                        userFromContext.getRole().getId(),
                        statusRepository.findByName(StatusEnum.COMMUNITY_LEADER).getId(),
                        userFromContext.getId(),
                        savedCommunity.getId()))
                .build();

        if (communityPersistRequest.getUsersId() != null) {
            userService.addUsersToRoleStatusUserCommunity(communityPersistRequest.getUsersId(), savedCommunity);
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
        List<RoleStatusUserCommunity> roleStatusUserCommunities = getRoleStatusUserCommunities(name, page, size);
        List<UserEntity> userEntities = roleStatusUserCommunities.stream()
                .map(RoleStatusUserCommunity::getUser)
                .collect(Collectors.toList());
        return userEntities.stream()
                .map(u -> conversionService.convert(u, UserEntityOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentResponse> findCommunityContents(String name, int page, int size) {
        List<ContentCommunity> userContentCommunities = getContentCommunities(name, page, size);
        List<Content> contents = userContentCommunities.stream()
                .map(ContentCommunity::getContent)
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

        userService.addUsersToRoleStatusUserCommunity(communityPersistRequest.getUsersId(), community);

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

        List<ContentCommunity> userContentCommunities = community.getContentCommunities();

        for (ContentCommunity contentCommunity : userContentCommunities) {
            if (communityPersistRequest.getUsersId() != null && !communityPersistRequest.getUsersId().isEmpty()) {
                contentService.addContentsToContentCommunity(communityPersistRequest.getContentsId(), community);
                contentCommunityRepository.save(contentCommunity);
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

    private Community getByName(String name) {
        try {
            return communityRepository.findByName(name);
        } catch (IllegalArgumentException e) {
            log.error("Community not found");
            throw new NotFoundException("Content not found");
        }
    }

    private List<ContentCommunity> getContentCommunities(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return contentCommunityRepository
                .findAllByCommunityName(name, pageRequest);
    }

    private List<RoleStatusUserCommunity> getRoleStatusUserCommunities(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return roleStatusUserCommunityRepository
                .findByCommunityName(name, pageRequest);
    }
}
