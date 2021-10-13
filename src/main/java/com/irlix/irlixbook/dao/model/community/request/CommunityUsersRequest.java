package com.irlix.irlixbook.dao.model.community.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityUsersRequest {

    @NotBlank
    private String name;

    private List<UUID> usersId;
}
