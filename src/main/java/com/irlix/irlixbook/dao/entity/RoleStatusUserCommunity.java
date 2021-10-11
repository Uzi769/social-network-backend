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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("roleId")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("statusId")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("userId")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("communityId")
    private Community community;

    @Column(name = "join-date")
    @Builder.Default
    private LocalDateTime dateJoined = LocalDateTime.now();
}
