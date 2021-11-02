package com.irlix.irlixbook.dao.mapper.user;


import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.output.UserAuthResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import org.springframework.core.convert.converter.Converter;

public class UserEntityToUserAuthResponse implements Converter<UserEntity, UserAuthResponse> {

    @Override
    public UserAuthResponse convert(UserEntity userEntity) {
        return UserAuthResponse.builder()
                .id(userEntity.getId())
                .defaultCommunityId("empty field")//todo add default community
                .build();
    }

}
