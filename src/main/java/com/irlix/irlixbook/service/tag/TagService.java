package com.irlix.irlixbook.service.tag;

import com.irlix.irlixbook.dao.entity.Tag;
import com.irlix.irlixbook.dao.model.tag.TagInput;
import com.irlix.irlixbook.dao.model.tag.TagOutput;

import java.util.List;

public interface TagService {
    List<TagOutput> save(TagInput tagInput);

    TagOutput findById(Long id);

    List<TagOutput> findAll();

    Tag getByName(String name);
}
