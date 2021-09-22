package com.irlix.irlixbook.dao.mapper.content;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import com.irlix.irlixbook.dao.model.content.request.ContentPersistRequest;
import org.springframework.core.convert.converter.Converter;

public class ContentPersistRequestToContent implements Converter<ContentPersistRequest, Content> {

    @Override
    public Content convert(ContentPersistRequest request) {
        return Content.builder()
                .name(request.getName())
                .description(request.getDescription())
                .registrationLink(request.getRegistrationLink())
                .shortDescription(request.getShortDescription())
                .type(ContentType.valueOf(request.getType()))
                .eventDate(request.getEventDate())
                .author(request.getAuthor())
                .build();
    }

}
