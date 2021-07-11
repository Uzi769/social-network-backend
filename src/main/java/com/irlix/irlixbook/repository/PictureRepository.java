package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PictureRepository extends JpaRepository<Picture, UUID> {
}
