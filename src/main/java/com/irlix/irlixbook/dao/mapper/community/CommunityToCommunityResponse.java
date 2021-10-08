package com.irlix.irlixbook.dao.mapper.community;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Community;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.ContentCommunity;
import com.irlix.irlixbook.dao.entity.UserEntity;
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

        List<ContentCommunity> userContentCommunities = community.getUserContentCommunities();
        List<Content> contents = null;
        List<UserEntity> users = null;

        if (!CollectionUtils.isEmpty(userContentCommunities)) {

            contents = userContentCommunities.stream()
                    .map(ContentCommunity::getContent)
                    .collect(Collectors.toList());
            users = userContentCommunities.stream()
                    .map(ContentCommunity::getUser)
                    .collect(Collectors.toList());

            try {
                UserEntity currentUser = SecurityContextUtils.getUserFromContext();
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
