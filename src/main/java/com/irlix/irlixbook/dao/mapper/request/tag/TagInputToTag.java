package com.irlix.irlixbook.dao.mapper.request.tag;

import com.irlix.irlixbook.dao.entity.Tag;
import com.irlix.irlixbook.dao.model.tag.TagInput;
import org.springframework.core.convert.converter.Converter;

public class TagInputToTag implements Converter<TagInput, Tag> {

    @Override
    public Tag convert(TagInput tagInput) {
        return Tag.builder()
                .name(tagInput.getName())
                .build();
    }
}
