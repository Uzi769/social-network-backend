package com.irlix.irlixbook.config;

import com.irlix.irlixbook.dao.mapper.response.user.UserEntityConverterToOutputForUser;
import com.irlix.irlixbook.dao.mapper.request.PostInputToPost;
import com.irlix.irlixbook.dao.mapper.response.PostToPostOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ConversionConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new UserEntityConverterToOutputForUser());
        registry.addConverter(new PostInputToPost());
        registry.addConverter(new PostToPostOutput());
    }
}
