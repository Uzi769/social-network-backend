package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.RoleStatusUserCommunity;
import com.irlix.irlixbook.dao.entity.RoleStatusUserCommunityId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RoleStatusUserCommunityRepository extends JpaRepository<RoleStatusUserCommunity,
        RoleStatusUserCommunityId> {

    List<RoleStatusUserCommunity> findByCommunityName(String name, Pageable pageable);

    List<RoleStatusUserCommunity> findByCommunityName(String name);

    List<RoleStatusUserCommunity> findByDateJoinedBefore(LocalDateTime date);

    List<RoleStatusUserCommunity> findByUserName(String name);

    List<RoleStatusUserCommunity> findByUserId(UUID uuid);
}
