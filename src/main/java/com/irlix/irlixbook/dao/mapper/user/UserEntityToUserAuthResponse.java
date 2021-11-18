package com.irlix.irlixbook.dao.mapper.user;


import com.irlix.irlixbook.dao.entity.Community;
import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.dao.model.user.output.UserAuthResponse;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

public class UserEntityToUserAuthResponse implements Converter<User, UserAuthResponse> {



    @Override
    public UserAuthResponse convert(User user) {

        List<Community> userCommunities = user.getCommunities();
        String defaultCommunityId;

        return UserAuthResponse.builder()
                .id(user.getId())
                .defaultCommunityId(userCommunities.size() != 0 ? userCommunities.get(0).getId().toString()
                        : null)
                .build();
    }

}
