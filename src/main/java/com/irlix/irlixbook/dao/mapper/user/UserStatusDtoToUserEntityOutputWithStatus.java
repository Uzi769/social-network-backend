package com.irlix.irlixbook.dao.mapper.user;


import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.dao.model.user.UserStatusDTO;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutputWithStatus;
import org.springframework.core.convert.converter.Converter;

public class UserStatusDtoToUserEntityOutputWithStatus implements Converter<UserStatusDTO, UserEntityOutputWithStatus> {

    @Override
    public UserEntityOutputWithStatus convert(UserStatusDTO userStatusDTO) {
        User user = userStatusDTO.getUser();
        return UserEntityOutputWithStatus.builder()
                .id(user.getId())
                .surname(user.getSurname())
                .name(user.getName())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .phone(user.getPhone())
                .description(user.getDescription())
                .skype(user.getSkype())
                .telegram(user.getTelegram())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .faceBook(user.getFaceBook())
                .vk(user.getVk())
                .instagram(user.getInstagram())
                .linkedIn(user.getLinkedIn())
                .avatar(user.getAvatar())
                .role(user.getRole().getName() != null ? user.getRole().getName().name() : null)
                .blocked(user.getBlocked() != null)
                .registrationDate(user.getRegistrationDate())
                .status(userStatusDTO.getStatusEnum())
                .build();
    }

}
