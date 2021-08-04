package com.irlix.irlixbook.service.sticker;

import com.irlix.irlixbook.dao.entity.Sticker;
import com.irlix.irlixbook.dao.model.sticker.input.StickerUpdateRequest;
import com.irlix.irlixbook.dao.model.sticker.output.StickerResponse;

import java.util.List;

public interface StickerService {

    Sticker findOrCreate(String name);

    StickerResponse findByName(String name);

    StickerResponse save(String stickerName);

    List<StickerResponse> findAll();

    void deleteById(Long id);

    StickerResponse update(StickerUpdateRequest request);
}
