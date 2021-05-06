package com.irlix.irlixbook.dao.mapper.response.user;


import com.irlix.irlixbook.dao.entity.Photo;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.direction.DirectionOutput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class UserEntityToUserEntityOutput implements Converter<UserEntity, UserEntityOutput> {

    @Override
    public UserEntityOutput convert(UserEntity userEntity) {
        return UserEntityOutput.builder()
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .phone(userEntity.getPhone())
                .anotherPhone(userEntity.getAnotherPhone())
                .birthDate(userEntity.getBirthDate())
                .city(userEntity.getCity())
                .skype(userEntity.getSkype())
                .technologies(userEntity.getTechnologies())
                .telegram(userEntity.getTelegram())
                .photos(userEntity.getPhotos().stream()
                        .map(Photo::getUrl)
                        .collect(Collectors.toList()))
                .directionList(userEntity.getDirections().stream().map(direction -> DirectionOutput.builder()
                        .title(direction.getTitle())
                        .description(direction.getDescription())
                        .build())
                        .collect(Collectors.toList())
                )
                .build();
    }
}
