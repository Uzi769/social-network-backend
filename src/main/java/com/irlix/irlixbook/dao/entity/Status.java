package com.irlix.irlixbook.dao.entity;

import com.irlix.irlixbook.dao.entity.enams.StatusEnum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "status")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column
    private StatusEnum name;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private List<Permission> permissions;

    @OneToMany(mappedBy = "status")
    private List<RoleStatusUserCommunity> roleStatusUserCommunities;

}
