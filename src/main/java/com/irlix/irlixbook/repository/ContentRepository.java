package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Content;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
