package com.irlix.irlixbook.dao.model.chat.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MessageRequest {

    private Long localId;
    private LocalDateTime timeStamp;
    private String content;
    private UUID userId;
}
