package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_content_community")
@Getter
@Setter
public class UserContentCommunity {

    @EmbeddedId
    private UserContentCommunityId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("communityId")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("userId")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("contentId")
    private Content content;

    @Column(name = "created_on")
    @Builder.Default
    private LocalDateTime dateCreated = LocalDateTime.now();
}
