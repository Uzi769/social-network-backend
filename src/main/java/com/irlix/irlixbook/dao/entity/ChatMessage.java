package com.irlix.irlixbook.dao.entity;

import com.irlix.irlixbook.dao.entity.enams.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {

    @Id
    private UUID id;

    private UUID chatId;
    private UUID senderId;
    private UUID localId;
    private MessageStatus status;
    private Date timestamp;
    private String content;
    // private MessageBody body;

}
