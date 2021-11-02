package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Community;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.entity.enams.StatusEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhone(String phone);

    List<UserEntity> findByBlockedLessThanEqual(LocalDateTime date);

    List<UserEntity> findByRegistrationDateLessThan(LocalDateTime date);

    List<UserEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<UserEntity> findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String name, String surname, Pageable pageable);

}
