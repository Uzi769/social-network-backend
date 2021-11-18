package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "role_status_user_community")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleStatusUserCommunity {

    @EmbeddedId
    private RoleStatusUserCommunityId Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("statusId")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId("communityId")
    private Community community;

    @Column(name = "join_date")
    @Builder.Default
    private LocalDateTime dateJoined = LocalDateTime.now();
}
