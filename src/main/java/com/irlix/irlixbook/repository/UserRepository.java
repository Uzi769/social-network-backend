package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(nativeQuery=true, value = "SELECT *\n" +
            "FROM user_entity\n" +
            "WHERE DATE(DATE_PART('year', CURRENT_DATE)||'-'||DATE_PART('month', birth_date)||'-'||DATE_PART('day', birth_date))\n" +
            "          BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 days';")
    List<UserEntity> findByBirthDate();

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhone(String phone);

    Optional<UserEntity> findByAnotherPhone(String phone);
}
