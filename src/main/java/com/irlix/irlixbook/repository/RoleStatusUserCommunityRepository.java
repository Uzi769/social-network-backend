package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.RoleStatusUserCommunity;
import com.irlix.irlixbook.dao.entity.RoleStatusUserCommunityId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleStatusUserCommunityRepository extends JpaRepository<RoleStatusUserCommunity,
        RoleStatusUserCommunityId> {

    List<RoleStatusUserCommunity> findByCommunityName(String name, Pageable pageable);
}
