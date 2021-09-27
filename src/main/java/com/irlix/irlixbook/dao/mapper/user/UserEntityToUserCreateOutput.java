package com.irlix.irlixbook.dao.mapper.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.output.UserAuthOutput;
import org.springframework.core.convert.converter.Converter;

public class UserEntityToUserCreateOutput implements Converter<UserEntity, UserAuthOutput> {

    @Override
    public UserAuthOutput convert(UserEntity userEntity) {
        return UserAuthOutput.builder()
                .id(userEntity.getId())
                .surname(userEntity.getSurname())
                .name(userEntity.getName())
                .avatar(userEntity.getAvatar())
                .build();
    }

}
