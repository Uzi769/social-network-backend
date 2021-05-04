package com.irlix.irlixbook.dao.mapper.response.user;


import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.UserEntityOutput;
import org.springframework.core.convert.converter.Converter;

public class UserEntityConverterToOutputForUser implements Converter<UserEntity, UserEntityOutput> {

    @Override
    public UserEntityOutput convert(UserEntity userEntity) {
        return UserEntityOutput.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .phone(userEntity.getPhone())
                .anotherPhone(userEntity.getAnotherPhone())
                .birthDate(userEntity.getBirthDate())
                .city(userEntity.getCity())
                .skype(userEntity.getSkype())
                .technologies(userEntity.getTechnologies())
                .telegram(userEntity.getTelegram())
                .build();
    }
}
