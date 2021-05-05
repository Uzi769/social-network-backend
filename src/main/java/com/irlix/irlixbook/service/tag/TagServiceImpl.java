package com.irlix.irlixbook.service.tag;

import com.irlix.irlixbook.dao.entity.Tag;
import com.irlix.irlixbook.dao.model.tag.TagInput;
import com.irlix.irlixbook.dao.model.tag.TagOutput;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ConversionService conversionService;

    private Tag getById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Tag not found");
                    return new NotFoundException("Tag not found");
                });
    }

    @Override
    public Tag getByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("Tag not found. Class TagServiceImpl, method getByName");
                    return new NotFoundException("Tag not found");
                });
    }

    @Override
    public void save(TagInput tagInput) {
        Tag tag = conversionService.convert(tagInput, Tag.class);
        if (tag == null) {
            log.error("TagInput cannot be null");
            throw new NullPointerException("TagInput cannot be null");
        }
        tagRepository.save(tag);
        log.info("Tag saved. Class TagServiceImpl, method save");
    }

    @Override
    public TagOutput findById(Long id) {
        TagOutput tagOutput = conversionService.convert(getById(id), TagOutput.class);
        log.info("Return Tag by id. Class TagServiceImpl, method findById");
        return tagOutput;
    }

    @Override
    public List<TagOutput> findAll() {
        List<TagOutput> tagOutputs = tagRepository.findAll().stream()
                .map(tag -> conversionService.convert(tag, TagOutput.class)).collect(Collectors.toList());
        return tagOutputs;
    }
}
