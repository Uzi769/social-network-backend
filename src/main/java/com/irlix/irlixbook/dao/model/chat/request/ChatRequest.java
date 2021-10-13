package com.irlix.irlixbook.dao.model.chat.request;

import com.irlix.irlixbook.dao.entity.UserEntity;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ChatRequest {

    private String title;
    private List<UserEntity> users;

}
