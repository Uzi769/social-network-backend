package com.irlix.irlixbook.repository;

import com.irlix.irlixbook.dao.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<ChatMessage, UUID> {

    List<ChatMessage> findByChatId(UUID chatId, Pageable pageable);

}
