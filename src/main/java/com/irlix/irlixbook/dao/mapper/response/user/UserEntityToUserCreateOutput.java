package com.irlix.irlixbook.dao.mapper.response.user;

import com.irlix.irlixbook.dao.entity.Photo;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.direction.DirectionOutput;
import com.irlix.irlixbook.dao.model.user.output.UserAuthOutput;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class UserEntityToUserCreateOutput implements Converter<UserEntity, UserAuthOutput> {

    @Override
    public UserAuthOutput convert(UserEntity userEntity) {
        return UserAuthOutput.builder()
                .id(userEntity.getId())
                .directionList(userEntity.getDirections().stream()
                        .map(direction -> DirectionOutput.builder()
                                .title(direction.getTitle())
                                .description(direction.getDescription())
                                .build()).collect(Collectors.toList()))
                .fullName(userEntity.getFullName())
                .photos(userEntity.getPhotos().stream()
                        .map(Photo::getUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}
