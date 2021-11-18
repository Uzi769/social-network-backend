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

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
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
    private final EntityManager entityManager;

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

        User userFromContext = SecurityContextUtils.getUserFromContext();
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
        Community community = getById(uuid);
        if (community != null) {
            CommunityResponse communityResponse = conversionService.convert(community, CommunityResponse.class);
            log.info("Return Community found by id. Class CommunityServiceImpl, method findById");
            return communityResponse;
        } else {
            throw new NotFoundException("There is no community with id " + uuid);
        }
    }

    @Override
    public CommunityResponse findByName(String name) {
        Community community = getByName(name);
        if (community != null) {
            CommunityResponse communityResponse = conversionService.convert(community, CommunityResponse.class);
            log.info("Return Community found by name. Class CommunityServiceImpl, method findByName");
            return communityResponse;
        } else {
            throw new NotFoundException("There is no community with name " + name);
        }
    }

    @Override
    @Transactional
    public List<UserEntityOutputWithStatus> findCommunityUsers(UUID id, int page, int size) {
        List<RoleStatusUserCommunity> roleStatusUserCommunities = getRoleStatusUserCommunities(id, page, size);

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
    public List<ContentResponse> findCommunityContents(UUID id, int page, int size) {
        if (communityRepository.findById(id).get() == null) {
            log.error("There are no community with given name.");
            throw new BadRequestException("There are no community with given name.");
        }

        List<ContentCommunity> userContentCommunities = getContentCommunities(id, page, size);
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
        Collection<Community> communities = new HashSet<>();
        communities.add(communityForDelete);

        if (name.equals("start")) {
            throw new IllegalArgumentException("You can't delete \"start\" community");
        }

        if (communityForDelete != null) {
            communityRepository.save(communityForDelete);
            deleteRoleStatusUserCommunities(communityForDelete);
            deleteContentCommunities(communityForDelete);
            communityRepository.deleteInBatch(communities);
            communityRepository.flush();
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

    @Override
    public List<UserEntityOutputWithStatus> findCommunityUsersByName(String name, int page, int size) {
        List<RoleStatusUserCommunity> roleStatusUserCommunities = getRoleStatusUserCommunitiesByName(name, page, size);

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
    public List<ContentResponse> findCommunityContentsByName(String name, int page, int size) {
        if (communityRepository.findByName(name) == null) {
            log.error("There are no community with given name.");
            throw new BadRequestException("There are no community with given name.");
        }

        List<ContentCommunity> userContentCommunities = getContentCommunitiesByName(name, page, size);
        List<Content> contents = userContentCommunities.stream()
                .map(ContentCommunity::getContent)
                .collect(Collectors.toList());
        return contents.stream()
                .map(u -> conversionService.convert(u, ContentResponse.class))
                .collect(Collectors.toList());

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

    private List<ContentCommunity> getContentCommunities(UUID id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return contentCommunityRepository
                .findAllByCommunityId(id, pageRequest);
    }

    private List<ContentCommunity> getContentCommunitiesByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return contentCommunityRepository
                .findAllByCommunityName(name, pageRequest);
    }

    private List<RoleStatusUserCommunity> getRoleStatusUserCommunities(UUID id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return roleStatusUserCommunityRepository
                .findByCommunityId(id, pageRequest);
    }

    private List<RoleStatusUserCommunity> getRoleStatusUserCommunitiesByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return roleStatusUserCommunityRepository
                .findByCommunityName(name, pageRequest);
    }

    private void deleteRoleStatusUserCommunities(Community community) {
        List<RoleStatusUserCommunity> forDelete = roleStatusUserCommunityRepository.findByCommunityName(community.getName());
        roleStatusUserCommunityRepository.deleteInBatch(forDelete);
    }

    private void deleteContentCommunities(Community community) {
        List<ContentCommunity> forDelete = contentCommunityRepository.findByCommunityName(community.getName());
        contentCommunityRepository.deleteInBatch(forDelete);
    }
}
