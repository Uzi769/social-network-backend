package com.irlix.irlixbook.config;

import com.irlix.irlixbook.dao.mapper.request.comment.CommentInputToComment;
import com.irlix.irlixbook.dao.mapper.request.tag.TagInputToTag;
import com.irlix.irlixbook.dao.mapper.response.comment.CommentToCommentOutput;
import com.irlix.irlixbook.dao.mapper.response.tag.TagToTagOutput;
import com.irlix.irlixbook.dao.mapper.response.user.UserEntityConverterToOutputForUser;
import com.irlix.irlixbook.dao.mapper.request.post.PostInputToPost;
import com.irlix.irlixbook.dao.mapper.response.post.PostToPostOutput;
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
        registry.addConverter(new CommentInputToComment());
        registry.addConverter(new TagInputToTag());
        registry.addConverter(new CommentToCommentOutput());
        registry.addConverter(new TagToTagOutput());
    }
}
