package com.irlix.irlixbook.dao.mapper.community;

import com.irlix.irlixbook.dao.entity.Community;
import com.irlix.irlixbook.dao.model.community.request.CommunityPersistRequest;
import org.springframework.core.convert.converter.Converter;

public class CommunityPersistRequestToCommunity implements Converter<CommunityPersistRequest, Community> {

    @Override
    public Community convert(CommunityPersistRequest request) {
        return Community.builder()
                .name(request.getName())
                .shortDescription(request.getShortDescription())
                .description(request.getDescription())
                .registrationLink(request.getRegistrationLink())
                .build();
    }
}
