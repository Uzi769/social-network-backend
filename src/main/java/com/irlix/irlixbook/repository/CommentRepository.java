package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
