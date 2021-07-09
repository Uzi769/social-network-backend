package com.irlix.irlixbook.service.sticker;

import com.irlix.irlixbook.dao.entity.Sticker;

public interface StickerService {

    Sticker findOrCreate(String name);

    Sticker findByName(String name);

    Sticker save(Sticker sticker);
}
