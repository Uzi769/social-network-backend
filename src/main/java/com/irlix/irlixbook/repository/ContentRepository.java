package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
}
