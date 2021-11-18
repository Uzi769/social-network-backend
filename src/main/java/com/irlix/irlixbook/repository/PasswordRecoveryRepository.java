package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.PasswordRecovery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, String> {

    List<PasswordRecovery> findByCreateDateLessThan(LocalDateTime date);

}
