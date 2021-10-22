package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.ContentCommunity;
import com.irlix.irlixbook.dao.entity.ContentCommunityId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentCommunityRepository extends JpaRepository<ContentCommunity, ContentCommunityId> {

    List<ContentCommunity> findAllByCommunityName(String name, Pageable pageable);

    List<ContentCommunity> findByCommunityName(String name);

    List<ContentCommunity> findByContent(Content content);

}
