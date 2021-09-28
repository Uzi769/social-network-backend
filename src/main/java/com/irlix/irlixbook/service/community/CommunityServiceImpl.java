package com.irlix.irlixbook.service.community;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Community;
import com.irlix.irlixbook.dao.entity.UserContentCommunityId;
import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.repository.CommunityRepository;
import com.irlix.irlixbook.repository.UserContentCommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{

    private final CommunityRepository communityRepository;
    private final UserContentCommunityRepository userContentCommunityRepository;
    private final ConversionService conversionService;

    @Value("${url.root}")
    private String urlRoot;

    @Override
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
        communityRepository.save(savedCommunity);

        log.info("Community saved. Class CommunityServiceImpl, method save");
        return conversionService.convert(savedCommunity, CommunityResponse.class);
    }

    @Override
    public List<CommunityResponse> findAll() {
        return null;
    }

    @Override
    public CommunityResponse findById(UUID uuid) {
        return null;
    }

    @Override
    public CommunityResponse findByName(String name) {
        return null;
    }

    @Override
    public List<UserEntityOutput> findCommunityUsers(String name) {
        return null;
    }

    @Override
    public List<ContentResponse> findCommunityContents(String name) {
        return null;
    }

    @Override
    public CommunityResponse addUsers(CommunityPersistRequest communityPersistRequest) {
        return null;
    }

    @Override
    public void delete(String name) {

    }

    @Override
    public CommunityResponse addContents(CommunityPersistRequest communityPersistRequest) {
        return null;
    }
}
