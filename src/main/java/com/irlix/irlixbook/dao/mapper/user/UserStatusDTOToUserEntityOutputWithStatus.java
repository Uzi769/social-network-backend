package com.irlix.irlixbook.dao.mapper.user;


import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.UserStatusDTO;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutputWithStatus;
import org.springframework.core.convert.converter.Converter;

public class UserStatusDTOToUserEntityOutputWithStatus implements Converter<UserStatusDTO, UserEntityOutputWithStatus> {

    @Override
    public UserEntityOutputWithStatus convert(UserStatusDTO userStatusDTO) {
        UserEntity userEntity = userStatusDTO.getUser();
        return UserEntityOutputWithStatus.builder()
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
                .role(userEntity.getRole().getName() != null ? userEntity.getRole().getName().name() : null)
                .blocked(userEntity.getBlocked() != null)
                .registrationDate(userEntity.getRegistrationDate())
                .status(userStatusDTO.getStatusEnum())
                .build();
    }

}
