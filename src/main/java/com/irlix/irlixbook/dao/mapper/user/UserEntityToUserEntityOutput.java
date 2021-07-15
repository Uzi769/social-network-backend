package com.irlix.irlixbook.dao.mapper.user;


import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import org.springframework.core.convert.converter.Converter;

public class UserEntityToUserEntityOutput implements Converter<UserEntity, UserEntityOutput> {

    @Override
    public UserEntityOutput convert(UserEntity userEntity) {
        return UserEntityOutput.builder()
                .id(userEntity.getId())
                .surname(userEntity.getSurname())
                .name(userEntity.getName())
                .birthDate(userEntity.getBirthDate())
                .gender(userEntity.getGender())
                .phone(userEntity.getPhone())
                .description(userEntity.getDescription())
                .skype(userEntity.getSkype())
                .telegram(userEntity.getTelegram())
                .avatar(userEntity.getAvatar())
                .email(userEntity.getEmail())
                .faceBook(userEntity.getFaceBook())
                .vk(userEntity.getVk())
                .instagram(userEntity.getInstagram())
                .linkedIn(userEntity.getLinkedIn())
                .avatar(userEntity.getAvatar())
                .blocked(userEntity.getBlocked() != null)
                .build();
    }
}
