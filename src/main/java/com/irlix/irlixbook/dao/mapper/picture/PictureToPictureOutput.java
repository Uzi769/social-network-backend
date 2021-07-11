package com.irlix.irlixbook.dao.mapper.picture;

import com.irlix.irlixbook.dao.entity.Picture;
import com.irlix.irlixbook.dao.model.picture.PictureOutput;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Collectors;

public class PictureToPictureOutput implements Converter<Picture, PictureOutput> {

    @Override
    public PictureOutput convert(Picture picture) {
        return PictureOutput.builder()
                .pictureId(picture.getId())
                .url(picture.getUrl())
                .contentId(picture.getContent() != null
                        ? picture.getContent().getId()
                        : null)
                .build();
    }
}
