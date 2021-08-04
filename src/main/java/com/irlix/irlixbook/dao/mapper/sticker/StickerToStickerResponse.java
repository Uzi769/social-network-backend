package com.irlix.irlixbook.dao.mapper.sticker;

import com.irlix.irlixbook.dao.entity.Sticker;
import com.irlix.irlixbook.dao.model.sticker.output.StickerResponse;
import org.springframework.core.convert.converter.Converter;

public class StickerToStickerResponse implements Converter<Sticker, StickerResponse> {

    @Override
    public StickerResponse convert(Sticker sticker) {
        return StickerResponse.builder()
                .id(sticker.getId())
                .name(sticker.getName())
                .build();
    }
}
