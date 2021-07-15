package com.irlix.irlixbook.dao.entity;

import com.irlix.irlixbook.dao.entity.enams.ContentType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "name", nullable = false, length = 1500)
    private String name;

    @Column(name = "type", nullable = false, length = 1500)
    @Enumerated(EnumType.STRING)
    private ContentType type;

    @Column(name = "short_description", length = 1500)
    private String shortDescription;

    @Column(name = "description", length = 10_000)
    private String description;

    @Column(name = "registration_link", length = 1500)
    private String registrationLink;

    @Column(name = "create_date")
    private LocalDateTime dateCreated;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private UserEntity author;

    @ManyToMany(mappedBy = "favoritesContents", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<UserEntity> users;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sticker_id")
    private Sticker sticker;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE)
    private List<Picture> pictures;
}
