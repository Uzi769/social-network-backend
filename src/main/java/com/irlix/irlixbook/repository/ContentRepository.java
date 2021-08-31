package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByNameContainingIgnoreCaseAndType(String name, ContentType contentType, Pageable pageable);

    List<Content> findByType(ContentType contentType, Pageable pageable);

    List<Content> findByType(ContentType contentType);

    List<Content> findByEventDateGreaterThanEqualAndEventDateLessThanAndType(LocalDateTime start, LocalDateTime end, ContentType type);

    @Query(value = "select c from Content c where c.type = :contentType order by case c.sticker.name when 'Важное' then 0 else 1 end")
    List<Content> findImportantContent(@Param("contentType") ContentType contentType, Pageable pageable);
}
