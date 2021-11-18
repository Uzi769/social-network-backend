package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_content")
public class ContentUser {

    @EmbeddedId
    private ContentUserId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("contentId")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("userId")
    private User user;

    @Column(name = "created_on")
    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

}
