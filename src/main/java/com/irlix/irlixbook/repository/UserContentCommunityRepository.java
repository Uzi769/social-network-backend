package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.UserContentCommunity;
import com.irlix.irlixbook.dao.entity.UserContentCommunityId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContentCommunityRepository extends JpaRepository<UserContentCommunity, UserContentCommunityId> {

    List<UserContentCommunity> findAllByCommunityName(String name, Pageable pageable);

}
