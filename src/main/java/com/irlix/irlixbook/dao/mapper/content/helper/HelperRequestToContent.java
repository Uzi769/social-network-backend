package com.irlix.irlixbook.dao.mapper.content.helper;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.model.content.helper.request.HelperRequest;
import org.springframework.core.convert.converter.Converter;

public class HelperRequestToContent implements Converter<HelperRequest, Content> {
    @Override
    public Content convert(HelperRequest helperRequest) {
        return Content.builder()
                .name(helperRequest.getTitle())
                .description(helperRequest.getDescription())
                .build();
    }
}
