package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommunityRepository extends JpaRepository<Community, UUID> {

    Community findByName(String name);

    Optional<Community> findById(UUID id);
}
