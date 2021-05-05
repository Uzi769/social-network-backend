package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Post;
import com.irlix.irlixbook.dao.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
