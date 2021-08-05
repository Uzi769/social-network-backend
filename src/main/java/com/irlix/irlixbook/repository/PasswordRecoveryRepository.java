package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.PasswordRecoveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecoveryEntity, String> {

    List<PasswordRecoveryEntity> findByCreateDateLessThan(LocalDateTime date);
}
