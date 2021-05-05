package com.irlix.irlixbook.dao.mapper.request.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.UserCreateInput;
import org.springframework.core.convert.converter.Converter;

public class UserCreateInputToUserEntity implements Converter<UserCreateInput, UserEntity> {

    @Override
    public UserEntity convert(UserCreateInput userCreateInput) {
        return UserEntity.builder()
                .fullName(userCreateInput.getFullName())
                .email(userCreateInput.getEmail())
                .phone(userCreateInput.getPhone())
                .password(userCreateInput.getPassword())
                .birthDate(userCreateInput.getBirthDate())
                .city(userCreateInput.getCity())
                .technologies(userCreateInput.getTechnologies())
                .build();
    }
}
