package com.irlix.irlixbook.dao.mapper.request.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import org.springframework.core.convert.converter.Converter;

public class UserCreateInputToUserEntity implements Converter<UserCreateInput, UserEntity> {

    @Override
    public UserEntity convert(UserCreateInput userCreateInput) {
        return UserEntity.builder()
                .surname(userCreateInput.getSurname())
                .name(userCreateInput.getName())
                .birthDate(userCreateInput.getBirthDate())
                .email(userCreateInput.getEmail())
                .telegram(userCreateInput.getGender())
                .build();
    }
}
