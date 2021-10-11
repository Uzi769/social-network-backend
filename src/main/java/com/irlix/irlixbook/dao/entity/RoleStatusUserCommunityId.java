package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RoleStatusUserCommunityId implements Serializable {

    @Column(name = "role_id")
    private int roleId;

    @Column(name = "status_id")
    private int statusId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "community_id")
    private UUID communityId;

}
