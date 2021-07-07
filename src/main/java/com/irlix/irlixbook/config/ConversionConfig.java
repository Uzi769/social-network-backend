package com.irlix.irlixbook.config;

import com.irlix.irlixbook.dao.mapper.request.post.PostInputToPost;
import com.irlix.irlixbook.dao.mapper.request.user.UserCreateInputToUserEntity;
import com.irlix.irlixbook.dao.mapper.request.user.UserUpdateInputToUserEntity;
import com.irlix.irlixbook.dao.mapper.response.post.PostToPostOutput;
import com.irlix.irlixbook.dao.mapper.response.user.UserEntityToUserCreateOutput;
import com.irlix.irlixbook.dao.mapper.response.user.UserEntityToUserEntityOutput;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConversionConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new UserEntityToUserEntityOutput());
        registry.addConverter(new UserCreateInputToUserEntity());
        registry.addConverter(new UserEntityToUserCreateOutput());
        registry.addConverter(new UserUpdateInputToUserEntity());
        registry.addConverter(new PostInputToPost());
        registry.addConverter(new PostToPostOutput());
    }
}
