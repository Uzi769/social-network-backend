package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Post, Long> {
}
