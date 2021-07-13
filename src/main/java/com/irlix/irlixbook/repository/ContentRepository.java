package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.enams.ContentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByNameContainingIgnoreCaseAndType(String name, ContentType contentType, Pageable pageable);
}
