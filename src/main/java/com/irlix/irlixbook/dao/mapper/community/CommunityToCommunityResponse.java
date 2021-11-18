package com.irlix.irlixbook.dao.mapper.community;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.*;
import com.irlix.irlixbook.dao.model.community.response.CommunityResponse;
import com.irlix.irlixbook.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CommunityToCommunityResponse implements Converter<Community, CommunityResponse> {

    @Override
    public CommunityResponse convert(Community community) {

        List<ContentCommunity> contentCommunities = community.getContentCommunities();
        List<RoleStatusUserCommunity> roleStatusUserCommunities = community.getRoleStatusUserCommunities();
        List<Content> contents = null;
        List<User> users = null;

        if (!CollectionUtils.isEmpty(contentCommunities)) {

            contents = contentCommunities.stream()
                    .map(ContentCommunity::getContent)
                    .collect(Collectors.toList());
            users = roleStatusUserCommunities.stream()
                    .map(RoleStatusUserCommunity::getUser)
                    .collect(Collectors.toList());

            try {
                User currentUser = SecurityContextUtils.getUserFromContext();
            } catch (UnauthorizedException e) {
                log.error("Convert content to content response without authorization");
            }
        }

        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .shortDescription(community.getShortDescription())
                .description(community.getDescription())
                .registrationLink(community.getRegistrationLink())
                .deepLink(community.getDeeplink())
                .admin(community.getCreator() != null
                        ? community.getCreator().getName()
                        : null)
                .contents(contents != null
                        ? contents.stream()
                        .map(Content::getId)
                        .collect(Collectors.toList())
                        : null)
                .users(users != null
                        ? users.stream()
                        .map(user -> user.getId().toString())
                        .collect(Collectors.toList())
                        : null)
                .creator(community.getCreator() != null
                        ? community.getCreator().getId().toString()
                        : null)
                .build();
    }
}
