package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Status;
import com.irlix.irlixbook.dao.entity.enams.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    Status findByName(StatusEnum status);

}
