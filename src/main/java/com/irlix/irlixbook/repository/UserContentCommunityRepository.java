package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.UserContentCommunity;
import com.irlix.irlixbook.dao.entity.UserContentCommunityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserContentCommunityRepository extends JpaRepository<UserContentCommunity, UserContentCommunityId> {

}
