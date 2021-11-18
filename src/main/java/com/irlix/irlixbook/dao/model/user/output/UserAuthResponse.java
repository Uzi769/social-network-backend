package com.irlix.irlixbook.dao.model.user.output;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthResponse {

    private UUID id;

    private String defaultCommunityId;
}
