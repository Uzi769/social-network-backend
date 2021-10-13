package com.irlix.irlixbook.service.community;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.*;
import com.irlix.irlixbook.dao.entity.enams.StatusEnum;
import com.irlix.irlixbook.dao.model.community.request.CommunityContentsRequest;
import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import com.irlix.irlixbook.dao.model.community.request.CommunityUsersRequest;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.dao.model.user.UserStatusDTO;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutputWithStatus;
import com.irlix.irlixbook.exception.AlreadyExistException;
import com.irlix.irlixbook.exception.BadRequestException;
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

import java.time.LocalDateTime;
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

        if (communityRepository.findByName(community.getName()) != null) {
            throw new AlreadyExistException("Community with name " + community.getName() + " already exist.");
        }

        //todo add sticker

        UserEntity userFromContext = SecurityContextUtils.getUserFromContext();
        community.setCreator(userFromContext);

        Community savedCommunity = communityRepository.save(community);
        savedCommunity.setDeeplink(urlRoot + savedCommunity.getName() + "/" + savedCommunity.getId());
        savedCommunity.setRegistrationLink(urlRoot + savedCommunity.getName() + "/" + savedCommunity.getId());
        savedCommunity.setCreatingDate(LocalDateTime.now());
        communityRepository.save(savedCommunity);

        if (communityPersistRequest.getContentsId() != null) {
            contentService.addContentsToContentCommunity(communityPersistRequest.getContentsId(), savedCommunity);
        }

        Role role = userFromContext.getRole();

        RoleStatusUserCommunity roleStatusUserCommunity = RoleStatusUserCommunity.builder()
                .role(role)
                .status(statusRepository.findByName(StatusEnum.COMMUNITY_LEADER))
                .user(userFromContext)
                .community(savedCommunity)
                .Id(new RoleStatusUserCommunityId(
                        role.getId(),
                        statusRepository.findByName(StatusEnum.COMMUNITY_LEADER).getId(),
                        userFromContext.getId(),
                        savedCommunity.getId()))
                .build();
        roleStatusUserCommunityRepository.save(roleStatusUserCommunity);

        if (communityPersistRequest.getUsersId() != null) {
            List<RoleStatusUserCommunity> roleStatusUserCommunities =
                    userService.addUsersToRoleStatusUserCommunity(communityPersistRequest.getUsersId(), savedCommunity);
            roleStatusUserCommunityRepository.saveAll(roleStatusUserCommunities);
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
    @Transactional
    public List<UserEntityOutputWithStatus> findCommunityUsers(String name, int page, int size) {
        List<RoleStatusUserCommunity> roleStatusUserCommunities = getRoleStatusUserCommunities(name, page, size);

        List<UserStatusDTO> userStatusDTOS = roleStatusUserCommunities.stream()
                .map(u -> UserStatusDTO.builder()
                        .user(u.getUser())
                        .statusEnum(u.getStatus().getName())
                        .build())
                .collect(Collectors.toList());

        return userStatusDTOS.stream()
                .map(u -> conversionService.convert(u, UserEntityOutputWithStatus.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ContentResponse> findCommunityContents(String name, int page, int size) {
        if (communityRepository.findByName(name) == null) {
            log.error("There are no community with given name.");
            throw new BadRequestException("There are no community with given name.");
        }

        List<ContentCommunity> userContentCommunities = getContentCommunities(name, page, size);
        List<Content> contents = userContentCommunities.stream()
                .map(ContentCommunity::getContent)
                .collect(Collectors.toList());
        return contents.stream()
                .map(u -> conversionService.convert(u, ContentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CommunityResponse addUsers(CommunityUsersRequest communityUsersRequest) {
        Community community = communityRepository.findByName(communityUsersRequest.getName());

        if (community == null) {
            log.error("There are no community with given name.");
            throw new BadRequestException("There are no community with given name.");
        }

        if (communityUsersRequest.getUsersId() != null) {
            List<RoleStatusUserCommunity> roleStatusUserCommunities =
                    userService.addUsersToRoleStatusUserCommunity(communityUsersRequest.getUsersId(), community);
            roleStatusUserCommunityRepository.saveAll(roleStatusUserCommunities);
        }

        log.info("Users saved to community. Class CommunityServiceImpl, method addUsers()");
        return conversionService.convert(community, CommunityResponse.class);
    }

    @Override
    @Transactional
    public void delete(String name) {

        Community communityForDelete = getByName(name);

        if (name.equals("start")) {
            throw new IllegalArgumentException("You can't delete \"start\" community");
        }

        if (communityForDelete != null) {
            communityRepository.delete(communityForDelete);
            log.info("Community deleted. Class CommunityServiceImpl, method delete");
        } else {
            throw new NotFoundException("Community with name " + name +  "not found.");
        }
    }

    @Override
    @Transactional
    public CommunityResponse addContents(CommunityContentsRequest communityContentsRequest) {
        Community community = communityRepository.findByName(communityContentsRequest.getName());

        if (community == null) {
            log.error("There are no community with given name.");
            throw new BadRequestException("There are no community with given name.");
        }

        if (communityContentsRequest.getContentsId() != null) {
            List<ContentCommunity> contentCommunities =
                    contentService.addContentsToContentCommunity(communityContentsRequest.getContentsId(), community);
            contentCommunityRepository.saveAll(contentCommunities);
        }

        log.info("Contents saved to community. Class CommunityServiceImpl, method addContents()");
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
