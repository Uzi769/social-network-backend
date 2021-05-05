package com.irlix.irlixbook.service.tag;

import com.irlix.irlixbook.dao.model.tag.TagInput;
import com.irlix.irlixbook.dao.model.tag.TagOutput;

import java.util.List;

public interface TagService {
    void save(TagInput tagInput);

    TagOutput findById(Long id);

    List<TagOutput> findAll();
}
