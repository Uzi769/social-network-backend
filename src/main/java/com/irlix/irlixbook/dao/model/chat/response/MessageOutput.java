package com.irlix.irlixbook.dao.model.chat.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MessageOutput {

    private UUID id;

    private Long localId;

    private LocalDateTime timeStamp;

    private String content;
}
