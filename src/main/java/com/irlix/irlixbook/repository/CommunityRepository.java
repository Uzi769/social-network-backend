package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommunityRepository extends JpaRepository<Community, UUID> {
}
