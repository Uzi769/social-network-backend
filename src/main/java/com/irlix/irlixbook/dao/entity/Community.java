package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community")
@Builder
@Getter
@Setter
public class Community {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "short_description", length = 1500)
    private String shortDescription;

    @Column(name = "description", length = 3000)
    private String description;

    @Column(name = "registration_link", length = 1500)
    private String registrationLink;

    @Column(name = "deeplink", length = 1500)
    private String deeplink;

    @Column(name = "admin")
    private String admin;

    @OneToMany(mappedBy = "community")
    private List<ContentCommunity> contentCommunities;

    @OneToMany(mappedBy = "community")
    private List<RoleStatusUserCommunity> roleStatusUserCommunities;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private UserEntity creator;
}
