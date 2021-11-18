package com.irlix.irlixbook.dao.entity;

import com.irlix.irlixbook.dao.entity.enams.RoleEnum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column
    private RoleEnum name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.MERGE)
    private List<User> users;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private List<Permission> permissions;

    @OneToMany(mappedBy = "role")
    private List<RoleStatusUserCommunity> roleStatusUserCommunities;
}
