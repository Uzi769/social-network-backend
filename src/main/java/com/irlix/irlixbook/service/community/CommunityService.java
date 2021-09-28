package com.irlix.irlixbook.service.community;

import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;

import java.util.List;
import java.util.UUID;

public interface CommunityService {
    CommunityResponse save(CommunityPersistRequest communityPersistRequest);

    List<CommunityResponse> findAll();

    CommunityResponse findById(UUID uuid);

    CommunityResponse findByName(String name);

    List<UserEntityOutput> findCommunityUsers(String name);

    List<ContentResponse> findCommunityContents(String name);

    CommunityResponse addUsers(CommunityPersistRequest communityPersistRequest);

    void delete(String name);

    CommunityResponse addContents(CommunityPersistRequest communityPersistRequest);
}
