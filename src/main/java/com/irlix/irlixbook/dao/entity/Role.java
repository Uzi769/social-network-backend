package com.irlix.irlixbook.dao.entity;

import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleEnum name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.MERGE)
    private List<UserEntity> users;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private List<Permission> permissions;

    @OneToMany(mappedBy = "role", cascade = CascadeType.MERGE)
    private List<RoleStatusUserCommunity> roleStatusUserCommunities;
}
