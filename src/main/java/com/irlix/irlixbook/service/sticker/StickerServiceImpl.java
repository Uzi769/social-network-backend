package com.irlix.irlixbook.service.sticker;

import com.irlix.irlixbook.dao.entity.Sticker;
import com.irlix.irlixbook.dao.model.sticker.input.StickerUpdateRequest;
import com.irlix.irlixbook.dao.model.sticker.output.StickerResponse;
import com.irlix.irlixbook.exception.NotFoundException;
import com.irlix.irlixbook.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StickerServiceImpl implements StickerService {

    private final StickerRepository stickerRepository;
    private final ConversionService conversionService;

    @Override
    public Sticker findOrCreate(String name) {

        if (!StringUtils.hasLength(name)) {
            throw new IllegalArgumentException("Sticker name is nul or empty");
        }
        return stickerRepository.findByName(name).orElseGet(() -> stickerRepository.save(
                Sticker.builder()
                        .name(name)
                        .build()
        ));

    }

    @Override
    public StickerResponse findByName(String name) {

        if (!StringUtils.hasLength(name)) {
            throw new IllegalArgumentException("Sticker name is nul or empty");
        }

        Optional<Sticker> byName = stickerRepository.findByName(name);
        return byName.map(sticker -> conversionService.convert(sticker, StickerResponse.class)).orElse(null);

    }

    @Override
    public StickerResponse save(String stickerName) {

        if (Objects.isNull(stickerName)) {
            throw new IllegalArgumentException("Sticker name is null");
        }

        Sticker save = stickerRepository.save(Sticker.builder()
                .name(stickerName)
                .build());

        return conversionService.convert(save, StickerResponse.class);

    }

    @Override
    public List<StickerResponse> findAll() {

        List<Sticker> all = stickerRepository.findAll();
        return all.stream()
                .map(s -> conversionService.convert(s, StickerResponse.class))
                .collect(Collectors.toList());

    }

    @Override
    public void deleteById(Long id) {
        stickerRepository.deleteById(id);
    }

    @Override
    public StickerResponse update(StickerUpdateRequest request) {

        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("Request for update sticker or id is null: " + request);
        }

        Optional<Sticker> byId = stickerRepository.findById(request.getId());

        if (byId.isEmpty()) {
            throw new NotFoundException("Sticker with id " + request.getId() + ", not found");
        } else {
            Sticker sticker = byId.get();
            sticker.setName(request.getName());
            Sticker save = stickerRepository.save(sticker);
            return conversionService.convert(save, StickerResponse.class);
        }

    }

}
