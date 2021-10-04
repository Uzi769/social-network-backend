package com.irlix.irlixbook.dao.model.chat.response;

import com.irlix.irlixbook.dao.model.user.output.UserEntityOutput;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatOutput {

    private UUID chatId;
    private String title;
    List<UserEntityOutput> users;
}
