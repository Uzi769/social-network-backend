package com.irlix.irlixbook.dao.helper;

import com.irlix.irlixbook.dao.entity.Sticker;
import com.irlix.irlixbook.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class StickerHelper {

    private final StickerRepository stickerRepository;

    public Sticker findOrCreate(String name) {

        if (!StringUtils.hasLength(name)) {
            throw new IllegalArgumentException("Sticker name is null or empty");
        }

        return stickerRepository.findByName(name).orElseGet(() -> stickerRepository.save(
                Sticker.builder()
                        .name(name)
                        .build()
        ));
    }
}
