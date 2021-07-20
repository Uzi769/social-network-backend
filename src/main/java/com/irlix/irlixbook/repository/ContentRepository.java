package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByNameContainingIgnoreCaseAndType(String name, ContentType contentType, Pageable pageable);

    List<Content> findByType(ContentType contentType, Pageable pageable);

    List<Content> findByUsers_Id(UUID userId, Pageable pageable);

    List<Content> findByUsers_IdAndType(UUID userId, ContentType contentType, Pageable pageable);

    List<Content> findByEventDateGreaterThanEqualAndEventDateLessThanAndType(LocalDateTime start, LocalDateTime end, ContentType type);
}
