package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    List<User> findByBlockedLessThanEqual(LocalDateTime date);

    List<User> findByRegistrationDateLessThan(LocalDateTime date);

    List<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<User> findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String name, String surname, Pageable pageable);

}
