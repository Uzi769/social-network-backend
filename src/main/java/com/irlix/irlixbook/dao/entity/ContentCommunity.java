package com.irlix.irlixbook.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content_community")
@Getter
@Setter
@Builder
public class ContentCommunity {

    @EmbeddedId
    private ContentCommunityId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("communityId")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("contentId")
    private Content content;
}
