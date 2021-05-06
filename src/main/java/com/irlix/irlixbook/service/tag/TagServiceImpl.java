package com.irlix.irlixbook.service.tag;

import com.irlix.irlixbook.dao.entity.Tag;
import com.irlix.irlixbook.dao.model.tag.TagInput;
import com.irlix.irlixbook.dao.model.tag.TagOutput;
import com.irlix.irlixbook.exception.BadRequestException;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ConversionService conversionService;

    private static final String TAG_NOT_FOUND = "Tag not found";

    private Tag getById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TAG_NOT_FOUND);
                    return new NotFoundException(TAG_NOT_FOUND);
                });
    }

    @Override
    public Tag getByName(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("Tag not found. Class TagServiceImpl, method getByName");
                    return new NotFoundException(TAG_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void save(TagInput tagInput) {
        if (tagInput == null) {
            log.error("TagInput cannot be null");
            throw new NullPointerException("TagInput cannot be null");
        }
        if (!tagRepository.findByName(tagInput.getName()).isEmpty()) {
            log.error("This tag already exist");
            throw new BadRequestException("This tag already exist");
        }

        Tag tag = conversionService.convert(tagInput, Tag.class);

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
        return tagRepository.findAll().stream()
                .map(tag -> conversionService.convert(tag, TagOutput.class)).collect(Collectors.toList());
    }
}
