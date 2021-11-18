package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.dao.entity.enams.ContentTypeEnum;
import com.irlix.irlixbook.dao.entity.enams.HelperEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByNameContainingIgnoreCaseAndType(String name, ContentTypeEnum type, Pageable pageable);

    List<Content> findByType(ContentTypeEnum type, Pageable pageable);

    List<Content> findByType(ContentTypeEnum contentTypeEnum);

    List<Content> findByEventDateGreaterThanEqualAndEventDateLessThanAndType(LocalDateTime start, LocalDateTime end, ContentTypeEnum type);

    @Query(value = "select c from Content c where c.type = :contentType order by case c.sticker.name when 'Важное' then 0 else 1 end")
    List<Content> findImportantContent(@Param("contentType") ContentTypeEnum contentTypeEnum, Pageable pageable);

    List<Content> findByTypeAndAndHelperTypeAndDateCreatedBetween(ContentTypeEnum contentTypeEnum,
                                                                  HelperEnum helperType,
                                                                  LocalDateTime start,
                                                                  LocalDateTime end,
                                                                  Pageable pageable);

    List<Content> findByTypeAndHelperTypeAndCreator(ContentTypeEnum contentTypeEnum,
                                                    HelperEnum helperType,
                                                    User creator,
                                                    Pageable pageable);

    List<Content> findByTypeAndHelperType(ContentTypeEnum contentTypeEnum,
                                          HelperEnum helperType,
                                          Pageable pageable);
}
