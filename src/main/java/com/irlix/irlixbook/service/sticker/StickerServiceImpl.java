package com.irlix.irlixbook.service.sticker;

import com.irlix.irlixbook.dao.entity.Sticker;
import com.irlix.irlixbook.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class StickerServiceImpl implements StickerService {

    private final StickerRepository stickerRepository;

    @Override
    public Sticker findOrCreate(String name){
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Sticker name is nul or empty");
        }
        Sticker sticker = stickerRepository.findByName(name).orElseGet(() -> stickerRepository.save(
                Sticker.builder()
                        .name(name)
                        .build()
        ));
        return sticker;
    }

    @Override
    public Sticker findByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Sticker name is nul or empty");
        }
        return stickerRepository.findByName(name).orElse(null);
    }

    @Override
    public Sticker save(Sticker sticker){
        if(Objects.isNull(sticker)){
            throw new IllegalArgumentException("Sticker is null");
        }
        return stickerRepository.save(sticker);
    }
}
