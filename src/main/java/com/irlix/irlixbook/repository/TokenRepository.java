package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByValue(String value);

}
