package com.irlix.irlixbook.dao.mapper.response.tag;

import com.irlix.irlixbook.dao.entity.Tag;
import com.irlix.irlixbook.dao.model.tag.TagOutput;
import org.springframework.core.convert.converter.Converter;

public class TagToTagOutput implements Converter<Tag, TagOutput> {

    @Override
    public TagOutput convert(Tag tag) {
        return TagOutput.builder()
                .name(tag.getName())
                .build();
    }
}
