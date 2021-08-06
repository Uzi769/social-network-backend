package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.UserAppCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAppCodeRepository extends JpaRepository<UserAppCode, String> {
}
