package com.irlix.irlixbook.dao.mapper.response.user;

import com.irlix.irlixbook.dao.entity.Photo;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.direction.DirectionOutput;
import com.irlix.irlixbook.dao.model.user.output.UserCreateOutput;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class UserEntityToUserCreateOutput implements Converter<UserEntity, UserCreateOutput> {

    @Override
    public UserCreateOutput convert(UserEntity userEntity) {
        return UserCreateOutput.builder()
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
