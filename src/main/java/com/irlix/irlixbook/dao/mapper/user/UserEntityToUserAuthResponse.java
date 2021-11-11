package com.irlix.irlixbook.dao.mapper.user;


import com.irlix.irlixbook.dao.entity.Community;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.model.user.output.UserAuthResponse;
import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

public class UserEntityToUserAuthResponse implements Converter<UserEntity, UserAuthResponse> {



    @Override
    public UserAuthResponse convert(UserEntity userEntity) {

        List<Community> userCommunities = userEntity.getCommunities();
        String defaultCommunityId;

        return UserAuthResponse.builder()
                .id(userEntity.getId())
                .defaultCommunityId(userCommunities.size() != 0 ? userCommunities.get(0).getId().toString()
                        : null)
                .build();
    }

}
