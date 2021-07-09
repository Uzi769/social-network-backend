package com.irlix.irlixbook.dao.mapper.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.input.UserUpdateInput;
import org.springframework.core.convert.converter.Converter;

public class UserUpdateInputToUserEntity implements Converter<UserUpdateInput, UserEntity> {

    @Override
    public UserEntity convert(UserUpdateInput userUpdateInput) {
        return UserEntity.builder()
                .surname(userUpdateInput.getSurname())
                .name(userUpdateInput.getName())
                .phone(userUpdateInput.getPhone())
                .email(userUpdateInput.getEmail())
                .birthDate(userUpdateInput.getBirthDate())
                .skype(userUpdateInput.getSkype())
                .telegram(userUpdateInput.getTelegram())
                .faceBook(userUpdateInput.getFaceBook())
                .instagram(userUpdateInput.getInstagram())
                .linkedIn(userUpdateInput.getLinkedIn())
                .vk(userUpdateInput.getVk())
                .description(userUpdateInput.getDescription())
                .build();
    }
}
