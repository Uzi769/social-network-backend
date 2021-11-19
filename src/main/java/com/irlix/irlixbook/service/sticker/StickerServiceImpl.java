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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.irlix.irlixbook.utils.Consts.STICKER_NAME_IS_NULL;
import static com.irlix.irlixbook.utils.Consts.STICKER_NAME_IS_NULL_OR_EMPTY;

@Slf4j
@RequiredArgsConstructor
@Service
public class StickerServiceImpl implements StickerService {

    private final StickerRepository stickerRepository;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public Sticker findOrCreate(String name) {

        if (!StringUtils.hasLength(name)) {
            throw new IllegalArgumentException(STICKER_NAME_IS_NULL_OR_EMPTY);
        }
        return stickerRepository.findByName(name).orElseGet(() -> stickerRepository.save(
                Sticker.builder()
                        .name(name)
                        .build()
        ));

    }

    @Override
    @Transactional(readOnly = true)
    public StickerResponse findByName(String name) {

        if (!StringUtils.hasLength(name)) {
            throw new IllegalArgumentException(STICKER_NAME_IS_NULL_OR_EMPTY);
        }

        Optional<Sticker> byName = stickerRepository.findByName(name);
        return byName.map(sticker -> conversionService.convert(sticker, StickerResponse.class)).orElse(null);

    }

    @Override
    @Transactional
    public StickerResponse save(String stickerName) {

        if (Objects.isNull(stickerName)) {
            throw new IllegalArgumentException(STICKER_NAME_IS_NULL);
        }

        Sticker save = stickerRepository.save(Sticker.builder()
                .name(stickerName)
                .build());

        return conversionService.convert(save, StickerResponse.class);

    }

    @Override
    @Transactional(readOnly = true)
    public List<StickerResponse> findAll() {
        return stickerRepository.findAll()
                .stream()
                .map(s -> conversionService.convert(s, StickerResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        stickerRepository.deleteById(id);
    }

    @Override
    @Transactional
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
