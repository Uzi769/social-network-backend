package com.irlix.irlixbook.config.web;

import com.irlix.irlixbook.dao.mapper.community.CommunityPersistRequestToCommunity;
import com.irlix.irlixbook.dao.mapper.community.CommunityToCommunityResponse;
import com.irlix.irlixbook.dao.mapper.content.ContentPersistRequestToContent;
import com.irlix.irlixbook.dao.mapper.content.ContentToContentResponse;
import com.irlix.irlixbook.dao.mapper.picture.PictureToPictureOutput;
import com.irlix.irlixbook.dao.mapper.sticker.StickerToStickerResponse;
import com.irlix.irlixbook.dao.mapper.user.UserCreateInputToUserEntity;
import com.irlix.irlixbook.dao.mapper.user.UserEntityToUserCreateOutput;
import com.irlix.irlixbook.dao.mapper.user.UserEntityToUserEntityOutput;
import com.irlix.irlixbook.dao.mapper.user.UserUpdateInputToUserEntity;
import com.irlix.irlixbook.dao.mapper.util.PeriodConvertor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(new UserEntityToUserEntityOutput());
        registry.addConverter(new UserCreateInputToUserEntity());
        registry.addConverter(new UserEntityToUserCreateOutput());
        registry.addConverter(new UserUpdateInputToUserEntity());
        registry.addConverter(new ContentPersistRequestToContent());
        registry.addConverter(new ContentToContentResponse());
        registry.addConverter(new PictureToPictureOutput());
        registry.addConverter(new StickerToStickerResponse());
        registry.addConverter(new PeriodConvertor());
        registry.addConverter(new CommunityPersistRequestToCommunity());
        registry.addConverter(new CommunityToCommunityResponse());

    }

    /**
     * Disable cors
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*")
                .exposedHeaders("*")
                .allowedMethods("*")
                .allowedHeaders("*");

    }

}
