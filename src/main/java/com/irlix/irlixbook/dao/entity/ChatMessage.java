package com.irlix.irlixbook.dao.entity;

import com.irlix.irlixbook.dao.entity.enams.MessageStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "local_id")
    private Long localId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @Column(name = "message_status")
    @Enumerated(EnumType.STRING)
    private MessageStatusEnum status;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "content")
    private String content;

}
