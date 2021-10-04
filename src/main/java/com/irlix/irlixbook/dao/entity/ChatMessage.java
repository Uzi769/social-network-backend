package com.irlix.irlixbook.dao.entity;

import com.irlix.irlixbook.dao.entity.enams.MessageStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_message")
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity sender;

    @Column(name = "local_id")
    private Long localId;

    @Column(name = "message_status")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "content")
    private String content;
}
