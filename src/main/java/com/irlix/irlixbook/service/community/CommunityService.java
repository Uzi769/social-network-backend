package com.irlix.irlixbook.service.community;

import com.irlix.irlixbook.dao.model.community.request.CommunityContentsRequest;
import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import com.irlix.irlixbook.dao.model.community.request.CommunityUsersRequest;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutputWithStatus;

import java.util.List;
import java.util.UUID;

public interface CommunityService {
    CommunityResponse save(CommunityPersistRequest communityPersistRequest);

    List<CommunityResponse> findAll();

    CommunityResponse findById(UUID uuid);

    CommunityResponse findByName(String name);

    List<UserEntityOutputWithStatus> findCommunityUsers(UUID id, int page, int size);

    List<ContentResponse> findCommunityContents(UUID id, int page, int size);

    CommunityResponse addUsers(CommunityUsersRequest communityUsersRequest);

    void delete(String name);

    CommunityResponse addContents(CommunityContentsRequest communityContentsRequest);

    List<UserEntityOutputWithStatus> findCommunityUsersByName(String name, int page, int size);

    List<ContentResponse> findCommunityContentsByName(String name, int page, int size);
}
