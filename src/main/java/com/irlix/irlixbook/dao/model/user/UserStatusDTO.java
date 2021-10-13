package com.irlix.irlixbook.dao.model.user;

import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.dao.entity.enams.StatusEnum;
import lombok.*;

@Getter
@Setter
@Builder
public class UserStatusDTO {
    private UserEntity user;
    private StatusEnum statusEnum;

    public UserStatusDTO() {
    }

    public UserStatusDTO(UserEntity user, StatusEnum statusEnum) {
        this.user = user;
        this.statusEnum = statusEnum;
    }
}
