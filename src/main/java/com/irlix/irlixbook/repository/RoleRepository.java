package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Role;
import com.irlix.irlixbook.dao.entity.enams.RoleEnam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnam name);
}
