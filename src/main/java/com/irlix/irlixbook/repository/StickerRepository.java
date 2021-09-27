package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StickerRepository extends JpaRepository<Sticker, Long> {

    Optional<Sticker> findByName(String name);

}
