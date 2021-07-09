package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
