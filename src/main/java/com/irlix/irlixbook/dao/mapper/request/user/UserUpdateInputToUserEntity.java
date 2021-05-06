package com.irlix.irlixbook.dao.mapper.request.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;
import org.springframework.core.convert.converter.Converter;

public class UserUpdateInputToUserEntity implements Converter<UserUpdateInput, UserEntity> {

    @Override
    public UserEntity convert(UserUpdateInput userUpdateInput) {
        return UserEntity.builder()
                .fullName(userUpdateInput.getFullName())
                .phone(userUpdateInput.getPhone())
                .email(userUpdateInput.getEmail())
                .technologies(userUpdateInput.getTechnologies())
                .anotherPhone(userUpdateInput.getAnotherPhone())
                .city(userUpdateInput.getCity())
                .birthDate(userUpdateInput.getBirthDate())
                .skype(userUpdateInput.getSkype())
                .telegram(userUpdateInput.getTelegram())
                .build();
    }
}
