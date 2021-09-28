package com.irlix.irlixbook.service.community;

import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{

    @Override
    public CommunityResponse save(CommunityPersistRequest communityPersistRequest) {
        return null;
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
}
