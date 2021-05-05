package com.irlix.irlixbook.dao.mapper.response.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.UserBirthdaysOutput;
import org.springframework.core.convert.converter.Converter;

public class UserEntityToUserBirthdaysOutput implements Converter<UserEntity, UserBirthdaysOutput> {

    @Override
    public UserBirthdaysOutput convert(UserEntity userEntity) {
        return UserBirthdaysOutput.builder()
                .fullName(userEntity.getFullName())
                .birthDate(userEntity.getBirthDate())
                .build();
    }
}

