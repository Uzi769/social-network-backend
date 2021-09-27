package com.irlix.irlixbook.dao.entity;

import com.irlix.irlixbook.dao.entity.enams.PermissionEnum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission")
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private PermissionEnum name;

    @ManyToMany(mappedBy = "permissions", cascade = CascadeType.MERGE)
    private List<Role> roles;

}
