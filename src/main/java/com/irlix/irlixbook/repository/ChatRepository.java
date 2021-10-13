package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    Chat getById(UUID id);

}
