package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.ContentUser;
import com.irlix.irlixbook.dao.entity.ContentUserId;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ContentUserRepository extends JpaRepository<ContentUser, ContentUserId> {

    @Modifying
    @Transactional
    void deleteByContentId(Long contentId);

    List<ContentUser> findByUserId(UUID userId, Pageable pageable);

    List<ContentUser> findByUserIdAndContent_Type(UUID userId, ContentType contentType, Pageable pageable);

}
