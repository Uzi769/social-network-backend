package com.irlix.irlixbook.config;

import com.irlix.irlixbook.dao.mapper.request.comment.CommentInputToComment;
import com.irlix.irlixbook.dao.mapper.request.tag.TagInputToTag;
import com.irlix.irlixbook.dao.mapper.request.user.UserCreateInputToUserEntity;
import com.irlix.irlixbook.dao.mapper.request.user.UserUpdateInputToUserEntity;
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
        registry.addConverter(new UserCreateInputToUserEntity());
        registry.addConverter(new UserUpdateInputToUserEntity());
        registry.addConverter(new TagInputToTag());
        TagToTagOutput tagOutput = new TagToTagOutput();
        registry.addConverter(tagOutput);
        registry.addConverter(new PostInputToPost());
        registry.addConverter(new PostToPostOutput(tagOutput));
        registry.addConverter(new CommentInputToComment());
        registry.addConverter(new CommentToCommentOutput());

    }
}
