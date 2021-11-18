package com.irlix.irlixbook.dao.model.user;

import com.irlix.irlixbook.dao.entity.User;
import com.irlix.irlixbook.dao.entity.enams.StatusEnum;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusDTO {

    private User user;

    private StatusEnum statusEnum;
}
