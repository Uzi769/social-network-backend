package com.irlix.irlixbook.dao.mapper.content;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.Picture;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.content.response.ContentResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ContentToContentResponse implements Converter<Content, ContentResponse> {

    @Override
    public ContentResponse convert(Content content) {
        List<UserEntity> users = content.getUsers();
        UserEntity currentUser = SecurityContextUtils.getUserFromContext();
        boolean isFavorite = false;

        if (!CollectionUtils.isEmpty(users) && currentUser != null) {
            isFavorite = users.stream()
                    .anyMatch(u -> u.getId().equals(currentUser.getId()));
        }
        return ContentResponse.builder()
                .id(content.getId())
                .author(content.getAuthor())
                .creator(content.getCreator() != null
                        ? content.getCreator().getId().toString()
                        : null)
                .description(content.getDescription())
                .shortDescription(content.getShortDescription())
                .name(content.getName())
                .registrationLink(content.getRegistrationLink())
                .type(content.getType().name())
                .stickerName(content.getSticker() != null ? content.getSticker().getName() : null)
                .users(content.getUsers() != null
                        ? content.getUsers().stream().map(u -> u.getId().toString()).collect(Collectors.toList())
                        : null)
                .pictures(content.getPictures() != null
                        ? content.getPictures().stream().map(Picture::getUrl).collect(Collectors.toList())
                        : null)
                .eventDate(content.getEventDate())
                .deeplink(content.getDeeplink())
                .favorite(isFavorite)
                .createDate(content.getDateCreated())
                .build();
    }
}
