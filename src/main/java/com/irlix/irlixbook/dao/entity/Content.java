package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content")
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description")
    private String description;

    @Column(name = "registration_link")
    private String registrationLink;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @ManyToMany(mappedBy = "favoritesContents", fetch = FetchType.LAZY)
    private List<UserEntity> users;

    @ManyToOne
    @JoinColumn(name = "sticker_id")
    private Sticker sticker;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "content")
    private List<Picture> pictures;
}
