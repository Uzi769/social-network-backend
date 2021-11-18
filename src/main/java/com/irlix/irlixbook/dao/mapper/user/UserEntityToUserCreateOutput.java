package com.irlix.irlixbook.dao.mapper.user;

import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.dao.model.user.output.UserAuthOutput;
import org.springframework.core.convert.converter.Converter;

public class UserEntityToUserCreateOutput implements Converter<User, UserAuthOutput> {

    @Override
    public UserAuthOutput convert(User user) {
        return UserAuthOutput.builder()
                .id(user.getId())
                .surname(user.getSurname())
                .name(user.getName())
                .avatar(user.getAvatar())
                .build();
    }

}
