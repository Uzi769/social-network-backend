package com.irlix.irlixbook.dao.mapper.user;

import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.dao.model.user.input.UserCreateInput;
import org.springframework.core.convert.converter.Converter;

public class UserCreateInputToUserEntity implements Converter<UserCreateInput, User> {

    @Override
    public User convert(UserCreateInput userCreateInput) {
        return User.builder()
                .surname(userCreateInput.getSurname())
                .name(userCreateInput.getName())
                .birthDate(userCreateInput.getBirthDate())
                .email(userCreateInput.getEmail())
                .gender(userCreateInput.getGender())
                .phone(userCreateInput.getPhone())
                .build();
    }

}
