package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Role;
import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleEnum name);
    List<Role> findByNameIn(List<RoleEnum> names);

}
