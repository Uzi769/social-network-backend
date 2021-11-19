package com.irlix.irlixbook.dao.helper;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.Picture;
import com.irlix.irlixbook.exception.ConflictException;
import com.irlix.irlixbook.repository.PictureRepository;
import com.irlix.irlixbook.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
@Slf4j
public class PictureHelper {

    private final PictureRepository pictureRepository;

    public List<Picture> addContentToPicture(List<UUID> pictureIdList, Content content) {

        List<Picture> pictures = new ArrayList<>();

        pictureIdList.stream()
                .map(id -> pictureRepository.findById(id).orElseThrow(() -> {
                    log.error(Consts.PICTURE_NOT_FOUND);
                    return new ConflictException(Consts.PICTURE_NOT_FOUND);
                }))
                .map(picture -> {
                    picture.setContent(content);
                    return pictureRepository.save(picture);
                })
                .collect(Collectors.toList());

        return pictures;
    }
}
